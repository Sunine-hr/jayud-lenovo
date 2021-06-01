package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.QueryOrderPickForm;
import com.jayud.mall.model.po.OrderPick;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.IOrderPickService;
import com.jayud.mall.utils.NumberGeneratedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单对应提货信息表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-17
 */
@Service
public class OrderPickServiceImpl extends ServiceImpl<OrderPickMapper, OrderPick> implements IOrderPickService {

    @Autowired
    OrderPickMapper orderPickMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    ShippingAreaMapper shippingAreaMapper;
    @Autowired
    OfferInfoMapper offerInfoMapper;
    @Autowired
    DeliveryAddressMapper deliveryAddressMapper;
    @Autowired
    OrderCaseMapper orderCaseMapper;

    @Override
    public List<OrderPickVO> createOrderPickList(List<DeliveryAddressVO> form) {
        List<OrderPickVO> orderPickVOList = new ArrayList<>();
        form.forEach(deliveryAddressVO -> {
            OrderPickVO orderPickVO = new OrderPickVO();
            String pickNo = NumberGeneratedUtils.getOrderNoByCode2("pick_no");
            orderPickVO.setPickNo(pickNo);//提货单号
            orderPickVO.setPickStatus(1);//提货状态(1未提货 2正在提货 3已提货 4已到仓)
            String warehouseNo = NumberGeneratedUtils.getWarehouseNo();
            orderPickVO.setWarehouseNo(warehouseNo);//进仓单号
            orderPickVO.setAddressId(deliveryAddressVO.getId());
            orderPickVO.setContacts(deliveryAddressVO.getContacts());
            orderPickVO.setPhone(deliveryAddressVO.getPhone());
            orderPickVO.setCountryName(deliveryAddressVO.getCountryName());
            orderPickVO.setStateName(deliveryAddressVO.getStateName());
            orderPickVO.setCityName(deliveryAddressVO.getCityName());
            orderPickVO.setAddress(deliveryAddressVO.getAddress());
            orderPickVOList.add(orderPickVO);
        });
        return orderPickVOList;
    }

    @Override
    public IPage<OrderPickVO> findOrderPickByPage(QueryOrderPickForm form) {
        //定义分页参数
        Page<OrderPickVO> page = new Page(form.getPageNum(),form.getPageSize());
        //定义排序规则
        page.addOrder(OrderItem.desc("t.id"));
        IPage<OrderPickVO> pageInfo = orderPickMapper.findOrderPickByPage(page, form);
        return pageInfo;
    }

    @Override
    public OrderWarehouseNoVO downloadWarehouseNoByPickId(Long id) {
        OrderPickVO orderPickVO = orderPickMapper.findOrderPickById(id);
        if(ObjectUtil.isEmpty(orderPickVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "提货单不存在");
        }
        String warehouseNo = orderPickVO.getWarehouseNo();
        Long orderId = orderPickVO.getOrderId();
        Integer totalCarton = orderPickVO.getTotalCarton();
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Integer offerInfoId = orderInfoVO.getOfferInfoId();
        String storeGoodsWarehouseCode = orderInfoVO.getStoreGoodsWarehouseCode();

        OfferInfoVO offerInfoVO = offerInfoMapper.lookOfferInfoFare(Long.valueOf(offerInfoId));
        if(ObjectUtil.isEmpty(offerInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "报价不存在");
        }
        LocalDateTime sailTime = offerInfoVO.getSailTime();
        LocalDateTime jcTime = offerInfoVO.getJcTime();
        ShippingAreaVO shippingAreaVO = shippingAreaMapper.findShippingAreaByWarehouseCode(storeGoodsWarehouseCode);
        if(ObjectUtil.isEmpty(shippingAreaVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "集货仓库不存在");
        }
        String addressFirst = shippingAreaVO.getAddressFirst();
        String contacts = shippingAreaVO.getContacts();
        String contactPhone = shippingAreaVO.getContactPhone();
        List<OrderCaseVO> orderCaseVOS = orderCaseMapper.findOrderCaseByOrderId(orderId);
        if(CollUtil.isNotEmpty(orderCaseVOS)){
            //todo
        }
        OrderWarehouseNoVO orderWarehouseNoVO = new OrderWarehouseNoVO();
        orderWarehouseNoVO.setWarehouseNo(warehouseNo);
        orderWarehouseNoVO.setSailTime(sailTime);
        orderWarehouseNoVO.setJcTime(jcTime);
        orderWarehouseNoVO.setTotalCarton(totalCarton);
        orderWarehouseNoVO.setWarehouseAddress(addressFirst);
        orderWarehouseNoVO.setContacts(contacts + " " + contactPhone);



        return null;
    }

    @Override
    public OrderWarehouseNoVO downloadWarehouseNoByOrderId(Long id) {
        //todo
        return null;
    }
}
