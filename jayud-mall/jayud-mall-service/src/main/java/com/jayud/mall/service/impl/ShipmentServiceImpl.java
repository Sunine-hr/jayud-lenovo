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
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.ShipmentMapper;
import com.jayud.mall.model.bo.QueryShipmentForm;
import com.jayud.mall.model.po.Shipment;
import com.jayud.mall.model.vo.ShipmentVO;
import com.jayud.mall.service.IShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ShipmentVO saveShipment(ShipmentVO shipmentVO) {
        String shipment_id = shipmentVO.getShipment_id();
        ShipmentVO shipment = shipmentMapper.findfindShipmentById(shipment_id);
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
                String shipmentJson = shipmentVO.getShipmentJson();
                if(StrUtil.isNotEmpty(shipmentJson)){
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
            });
        }
        return pageInfo;
    }

    @Override
    public CommonResult<ShipmentVO> findfindShipmentById(String shipment_id) {
        ShipmentVO shipmentVO = shipmentMapper.findfindShipmentById(shipment_id);
        if(ObjectUtil.isEmpty(shipmentVO)){
            return CommonResult.error(-1, "没有做找到新智慧对应的运单信息");
        }
        String shipmentJson = shipmentVO.getShipmentJson();
        if(StrUtil.isNotEmpty(shipmentJson)){
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
        return CommonResult.success(shipmentVO);
    }
}
