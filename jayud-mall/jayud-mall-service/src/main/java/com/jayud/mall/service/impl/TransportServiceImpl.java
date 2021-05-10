package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.DeliverInfoMapper;
import com.jayud.mall.mapper.OrderPickMapper;
import com.jayud.mall.mapper.ShippingAreaMapper;
import com.jayud.mall.mapper.TransportMapper;
import com.jayud.mall.model.bo.QueryTransportForm;
import com.jayud.mall.model.bo.TransportForm;
import com.jayud.mall.model.bo.TransportParaForm;
import com.jayud.mall.model.po.DeliverInfo;
import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.model.po.Transport;
import com.jayud.mall.model.vo.DeliverInfoVO;
import com.jayud.mall.model.vo.OrderPickVO;
import com.jayud.mall.model.vo.ShippingAreaVO;
import com.jayud.mall.model.vo.TransportVO;
import com.jayud.mall.service.IDeliverInfoService;
import com.jayud.mall.service.IOrderPickService;
import com.jayud.mall.service.ITransportService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 运输管理表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-04-06
 */
@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements ITransportService {

    @Autowired
    TransportMapper transportMapper;
    @Autowired
    ShippingAreaMapper shippingAreaMapper;
    @Autowired
    OrderPickMapper orderPickMapper;
    @Autowired
    DeliverInfoMapper deliverInfoMapper;

    @Autowired
    IOrderPickService orderPickService;
    @Autowired
    IDeliverInfoService deliverInfoService;

    @Override
    public IPage<TransportVO> findTransportByPage(QueryTransportForm form) {
        //定义分页参数
        Page<TransportVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<TransportVO> pageInfo = transportMapper.findTransportByPage(page, form);
        return pageInfo;
    }

    @Override
    public CommonResult<TransportVO> createTransport(List<OrderPickVO> form) {
        TransportVO transportVO = new TransportVO();
        if(CollUtil.isEmpty(form)){
            return CommonResult.error(-1, "提单不能为空");
        }
        for (int i=0; i<form.size(); i++){
            OrderPickVO orderPickVO = form.get(i);
            Long transportId = orderPickVO.getTransportId();
            if(ObjectUtil.isNotEmpty(transportId)){
                return CommonResult.error(-1, "提货单号["+orderPickVO.getPickNo()+"],已经拼车提货了");
            }
        }
        //拼车提货-运输信息
        String transportNo = NumberGeneratedUtils.getOrderNoByCode2("transport_no");
        transportVO.setTransportNo(transportNo);//运输单号

        //拼车提货-提货信息
        form.forEach(orderPickVO -> {
            //指定日期默认带出左侧提货时间，再允许修改；写入运输单的提货日期以修改后的为准
            LocalDateTime pickTime = orderPickVO.getPickTime();
            orderPickVO.setAssignPickTime(pickTime);//指定提货时间(后台拼车提货时指定)
        });
        transportVO.setOrderPickVOS(form);

        //评车提货-送货信息
        List<DeliverInfoVO> deliverInfoVOS = new ArrayList<>();
        form.forEach(orderPickVO -> {
            //1.订单提货表
            //提货id(order_pick id)
            Long orderPickId = orderPickVO.getId();
            //进仓单号(order_pick warehouse_no)
            String warehouseNo = orderPickVO.getWarehouseNo();
            //提货单号(order_pick pick_no)
            String pickNo = orderPickVO.getPickNo();
            //箱数(order_pick total_carton)
            Integer totalCarton = orderPickVO.getTotalCarton();

            //2.集货仓库表
            //集货仓库代码(shipping_area warehouse_code)
            String shippingWarehouseCode = orderPickVO.getShippingWarehouseCode();
            //集货仓库名称(shipping_area warehouse_name)
            String shippingWarehouseName = orderPickVO.getShippingWarehouseName();
            ShippingAreaVO shippingAreaVO = shippingAreaMapper.findShippingAreaByWarehouseCode(shippingWarehouseCode);
            //联系人(shipping_area contacts)
            String contacts = shippingAreaVO.getContacts();
            //联系手机(shipping_area contact_phone)
            String contactPhone = shippingAreaVO.getContactPhone();
            //送货地址 省 + 市 + 区 + 地址1
            String deliveryAddress = shippingAreaVO.getStateName() +" "+ shippingAreaVO.getCityName() +" "+ shippingAreaVO.getRegionName() +" "+ shippingAreaVO.getAddressFirst();

            DeliverInfoVO deliverInfoVO = new DeliverInfoVO();
            deliverInfoVO.setOrderPickId(orderPickId);
            deliverInfoVO.setWarehouseNo(warehouseNo);
            deliverInfoVO.setPickNo(pickNo);
            deliverInfoVO.setTotalCarton(totalCarton);
            deliverInfoVO.setScheduledTime(null);//预计时间
            deliverInfoVO.setWarehouseCode(shippingWarehouseCode);
            deliverInfoVO.setWarehouseName(shippingWarehouseName);
            deliverInfoVO.setContacts(contacts);
            deliverInfoVO.setContactPhone(contactPhone);
            deliverInfoVO.setDeliveryAddress(deliveryAddress);

            deliverInfoVOS.add(deliverInfoVO);
        });
        transportVO.setDeliverInfoVOS(deliverInfoVOS);

        return CommonResult.success(transportVO);
    }

    @Override
    public CommonResult<TransportVO> affirmTransport(TransportForm form) {
        Transport transport = ConvertUtil.convert(form, Transport.class);
        //提货信息
        List<OrderPickVO> orderPickVOS = form.getOrderPickVOS();
        List<OrderPick> orderPicks = ConvertUtil.convertList(orderPickVOS, OrderPick.class);
        //送货信息
        List<DeliverInfoVO> deliverInfoVOS = form.getDeliverInfoVOS();
        List<DeliverInfo> deliverInfos = ConvertUtil.convertList(deliverInfoVOS, DeliverInfo.class);


        transport.setTransportStatus(1);//运输状态(1在途 2已送达)
        this.saveOrUpdate(transport);
        Long transportId = transport.getId();

        orderPicks.forEach(orderPick -> {
            orderPick.setTransportId(transportId);
            orderPick.setPickStatus(2);//提货状态(1未提货 2正在提货 3已提货 4已到仓)
        });
        orderPickService.saveOrUpdateBatch(orderPicks);

        deliverInfos.forEach(deliverInfo -> {
            deliverInfo.setTransportId(transportId);
        });
        deliverInfoService.saveOrUpdateBatch(deliverInfos);

        TransportVO transportVO = ConvertUtil.convert(transport, TransportVO.class);
        return CommonResult.success(transportVO);
    }

    @Override
    public CommonResult<TransportVO> findTransport(TransportParaForm form) {
        Long id = form.getId();
        TransportVO transportVO = transportMapper.findTransportById(id);
        if(ObjectUtil.isEmpty(transportVO)){
            return CommonResult.error(-1, "运输单不存在");
        }
        Long transportId = transportVO.getId();
        //提货信息
        List<OrderPickVO> orderPickVOS = orderPickMapper.findOrderPickByTransportId(transportId);
        transportVO.setOrderPickVOS(orderPickVOS);
        //送货信息
        List<DeliverInfoVO> deliverInfoVOS = deliverInfoMapper.findDeliverInfoByTransportId(transportId);
        transportVO.setDeliverInfoVOS(deliverInfoVOS);

        return CommonResult.success(transportVO);
    }

    @Override
    public CommonResult<TransportVO> editAffirmTransport(TransportForm from) {
        Long id = from.getId();
        TransportVO transportVO = transportMapper.findTransportById(id);
        if(ObjectUtil.isEmpty(transportVO)){
            return CommonResult.error(-1, "运输单不存在");
        }

        //1.运输信息
        Transport transport = ConvertUtil.convert(from, Transport.class);
        this.saveOrUpdate(transport);

        //2.提货信息，仅展示，不修改
        List<OrderPickVO> orderPickVOS = from.getOrderPickVOS();
        //3.送货信息，仅展示，不修改
        List<DeliverInfoVO> deliverInfoVOS = from.getDeliverInfoVOS();

        transportVO = ConvertUtil.convert(transport, TransportVO.class);
        transportVO.setOrderPickVOS(orderPickVOS);
        transportVO.setDeliverInfoVOS(deliverInfoVOS);

        return CommonResult.success(transportVO);
    }

    @Override
    public CommonResult confirmDelivery(TransportParaForm form) {
        Long id = form.getId();
        TransportVO transportVO = transportMapper.findTransportById(id);
        if(ObjectUtil.isEmpty(transportVO)){
            return CommonResult.error(-1, "运输单不存在");
        }
        Transport transport = ConvertUtil.convert(transportVO, Transport.class);
        transport.setTransportStatus(2);//运输状态(1在途 2已送达)
        this.saveOrUpdate(transport);

        //提货信息
        List<OrderPickVO> orderPickVOS = orderPickMapper.findOrderPickByTransportId(id);
        List<OrderPick> orderPicks = ConvertUtil.convertList(orderPickVOS, OrderPick.class);
        orderPicks.forEach(orderPick -> {
            orderPick.setPickStatus(4);//提货状态(1未提货 2正在提货 3已提货 4已到仓)
        });
        orderPickService.saveOrUpdateBatch(orderPicks);

        return CommonResult.success("确认送达，成功");
    }

}
