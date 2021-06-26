package com.jayud.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.OrderServiceMapper;
import com.jayud.mall.model.bo.OrderServiceForm;
import com.jayud.mall.model.po.OrderService;
import com.jayud.mall.model.vo.OrderServiceReceivableVO;
import com.jayud.mall.model.vo.OrderServiceVO;
import com.jayud.mall.model.vo.OrderServiceWithVO;
import com.jayud.mall.service.IOrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public List<OrderServiceVO> findOrderServiceByOrderId(Long orderId) {
        return orderServiceMapper.findOrderServiceByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderService(OrderServiceForm form) {
        OrderService orderService = ConvertUtil.convert(form, OrderService.class);




    }

    @Override
    public OrderServiceVO findOrderServiceById(Long id) {
        OrderServiceVO orderServiceVO = orderServiceMapper.findOrderServiceById(id);
        List<OrderServiceReceivableVO> orderServiceReceivableList = new ArrayList<>();
        List<OrderServiceWithVO> orderServiceWithList = new ArrayList<>();
        orderServiceVO.setOrderServiceReceivableList(orderServiceReceivableList);
        orderServiceVO.setOrderServiceWithList(orderServiceWithList);
        return orderServiceVO;
    }
}
