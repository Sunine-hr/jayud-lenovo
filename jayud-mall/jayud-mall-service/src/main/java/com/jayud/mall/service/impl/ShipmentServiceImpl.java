package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.OrderEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderInfoMapper;
import com.jayud.mall.mapper.ShipmentMapper;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.OrderInfo;
import com.jayud.mall.model.po.Shipment;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.model.vo.ShipmentVO;
import com.jayud.mall.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * <p>
 * 南京新智慧-运单装货信息 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-09
 */
@Slf4j
@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements IShipmentService {

    @Autowired
    ShipmentMapper shipmentMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    IOrderCaseService orderCaseService;
    @Autowired
    IOrderShopService orderShopService;
    @Autowired
    ICustomerGoodsService customerGoodsService;

    @Override
    public ShipmentVO saveShipment(ShipmentVO shipmentVO) {
        String shipment_id = shipmentVO.getShipment_id();
        ShipmentVO shipment = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipment)){
            //新增插入
            log.info("新增插入:{}", shipmentVO);
        }else{
            //更新修改
            log.info("更新修改:{}", shipmentVO);
        }
        Shipment s = ConvertUtil.convert(shipmentVO, Shipment.class);
        if(ObjectUtil.isNotEmpty(shipmentVO.getPicking_time())){
            long l = shipmentVO.getPicking_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setPicking_time(ldt);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getRates_time())){
            long l = shipmentVO.getRates_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setRates_time(ldt);
        }
        if(ObjectUtil.isNotEmpty(shipmentVO.getCreat_time())){
            long l = shipmentVO.getCreat_time()*1000L;//秒转为毫秒
            LocalDateTime ldt = Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDateTime();
            s.setCreat_time(ldt);
        }
        s.setShipmentJson(JSONUtil.toJsonStr(shipmentVO));
        this.saveOrUpdate(s);
        return shipmentVO;
    }

    @Override
    public IPage<ShipmentVO> findShipmentByPage(QueryShipmentForm form) {
        //定义分页参数
        Page<ShipmentVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.shipment_id"));
        IPage<ShipmentVO> pageInfo = shipmentMapper.findShipmentByPage(page, form);
        List<ShipmentVO> records = pageInfo.getRecords();
        if(CollUtil.isNotEmpty(records)){
            records.forEach(shipmentVO -> {
                fit(shipmentVO);
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<ShipmentVO> findShipmentById(String shipment_id) {
        ShipmentVO shipmentVO = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipmentVO)){
            return CommonResult.error(-1, "没有做找到新智慧对应的运单信息");
        }
        fit(shipmentVO);
        return CommonResult.success(shipmentVO);
    }

    @Override
    public CommonResult<ShipmentVO> createOrderByShipment(String shipment_id) {
        ShipmentVO shipmentVO = shipmentMapper.findShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipmentVO)){
            return CommonResult.error(-1, "没有做找到新智慧对应的运单信息");
        }
        fit(shipmentVO);

        String orderNo = shipmentVO.getShipment_id();
        OrderInfoVO orderInfoVO = orderInfoMapper.findOrderInfoByOrderNo(orderNo);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNo(shipmentVO.getShipment_id());//订单号
            orderInfo.setCustomerId(shipmentVO.getCustomerId());//客户ID(customer id)

            orderInfo.setOfferInfoId(null);//报价id(offer_info id),没有关联的报价
            orderInfo.setReserveSize(null);//订柜尺寸,根据报价选择
            orderInfo.setStoreGoodsWarehouseCode(null);//集货仓库代码,根据报价选择
            orderInfo.setStoreGoodsWarehouseName(null);//集货仓库名称,根据报价选择
            orderInfo.setDestinationWarehouseCode(null);//目的仓库代码,根据报价选择
            orderInfo.setDestinationWarehouseName(null);//目的仓库名称,根据报价选择

            orderInfo.setIsPick(0);//是否上门提货(0否 1是),默认为否
            orderInfo.setStatus(OrderEnum.DRAFT.getCode());//状态码,默认为草稿状态
            orderInfo.setStatusName(OrderEnum.DRAFT.getName());//状态名称

            orderInfo.setCreateTime(shipmentVO.getCreatTime());//创建日期,新智慧的下单日期
            orderInfo.setCreateUserId(shipmentVO.getCustomerId());//创建人ID(customer id)
            orderInfo.setCreateUserName(shipmentVO.getCustomerUserName());//创建人名称(customer user_name)
            orderInfo.setOrderOrigin("2");//订单来源(1web端 2新智慧同步)

            orderInfo.setRemark("新智慧同步订单");
            orderInfo.setChargeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//收费重(KG)
            orderInfo.setVolumeWeight(new BigDecimal(shipmentVO.getChargeable_weight()));//材积重(KG),默认等于 收费重(KG)
            orderInfo.setActualVolume(null);//实际体积(m3),默认为空
            orderInfo.setTotalCartons(null);//总箱数,默认为空
            orderInfoService.saveOrUpdate(orderInfo);
        }

        return null;
    }

    /**
     * 组装数据
     * @param shipmentVO
     */
    private void fit(ShipmentVO shipmentVO) {
        String shipmentJson = shipmentVO.getShipmentJson();
        if (StrUtil.isNotEmpty(shipmentJson)) {
            ShipmentVO shipment = null;
            try {
                shipment = JSONUtil.toBean(shipmentJson, ShipmentVO.class);
                shipmentVO.setAttrs(shipment.getAttrs());
                shipmentVO.setTo_address(shipment.getTo_address());
                shipmentVO.setFrom_address(shipment.getFrom_address());
                shipmentVO.setCharge_list(shipment.getCharge_list());
                shipmentVO.setParcels(shipment.getParcels());
                shipmentVO.setPicking_time(shipment.getPicking_time());
                shipmentVO.setRates_time(shipment.getRates_time());
                shipmentVO.setCreat_time(shipment.getCreat_time());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
