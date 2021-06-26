package com.jayud.mall.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OrderServiceForm;
import com.jayud.mall.model.po.OrderService;
import com.jayud.mall.model.vo.OrderServiceReceivableVO;
import com.jayud.mall.model.vo.OrderServiceVO;
import com.jayud.mall.model.vo.OrderServiceWithVO;
import com.jayud.mall.service.IOrderCopeReceivableService;
import com.jayud.mall.service.IOrderCopeWithService;
import com.jayud.mall.service.IOrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 订单服务表 服务实现类
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-25
 */
@Service
public class OrderServiceServiceImpl extends ServiceImpl<OrderServiceMapper, OrderService> implements IOrderServiceService {

    @Autowired
    OrderServiceMapper orderServiceMapper;
    @Autowired
    OrderServiceReceivableMapper orderServiceReceivableMapper;
    @Autowired
    OrderServiceWithMapper orderServiceWithMapper;
    @Autowired
    OrderCopeReceivableMapper orderCopeReceivableMapper;
    @Autowired
    OrderCopeWithMapper orderCopeWithMapper;

    @Autowired
    IOrderCopeReceivableService orderCopeReceivableService;
    @Autowired
    IOrderCopeWithService orderCopeWithService;

    @Override
    public List<OrderServiceVO> findOrderServiceByOrderId(Long orderId) {
        return orderServiceMapper.findOrderServiceByOrderId(orderId);
    }

    /**
     * 保存订单服务
     * 1.服务下添加的应收、应付，要合并到订单应收、应付。
     * 2.服务下的应收、应付，增删改之后，订单应收、应付也要改动。
     * @param form
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderService(OrderServiceForm form) {
        OrderService orderService = ConvertUtil.convert(form, OrderService.class);




    }

    @Override
    public OrderServiceVO findOrderServiceById(Long id) {
        OrderServiceVO orderServiceVO = orderServiceMapper.findOrderServiceById(id);
        if(ObjectUtil.isEmpty(orderServiceVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单服务不存在");
        }
        Long qie = orderServiceVO.getId();//订单服务id(order_service id)
        List<OrderServiceReceivableVO> orderServiceReceivableList = orderServiceReceivableMapper.findOrderServiceReceivableByQie(qie);
        List<OrderServiceWithVO> orderServiceWithList = orderServiceWithMapper.findOrderServiceWithByQie(qie);
        orderServiceVO.setOrderServiceReceivableList(orderServiceReceivableList);
        orderServiceVO.setOrderServiceWithList(orderServiceWithList);
        return orderServiceVO;
    }
}
