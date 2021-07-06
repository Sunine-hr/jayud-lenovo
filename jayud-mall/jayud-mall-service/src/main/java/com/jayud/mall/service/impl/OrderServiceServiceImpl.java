package com.jayud.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.exception.Asserts;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.mall.mapper.*;
import com.jayud.mall.model.bo.OrderServiceForm;
import com.jayud.mall.model.bo.OrderServiceReceivableForm;
import com.jayud.mall.model.bo.OrderServiceWithForm;
import com.jayud.mall.model.po.*;
import com.jayud.mall.model.vo.*;
import com.jayud.mall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    OrderServiceReceivableMapper orderServiceReceivableMapper;
    @Autowired
    OrderServiceWithMapper orderServiceWithMapper;
    @Autowired
    OrderCopeReceivableMapper orderCopeReceivableMapper;
    @Autowired
    OrderCopeWithMapper orderCopeWithMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    IOrderServiceReceivableService orderServiceReceivableService;
    @Autowired
    IOrderServiceWithService orderServiceWithService;
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
        Long orderId = orderService.getOrderId();
        if(ObjectUtil.isEmpty(orderId)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单id不能为空");
        }
        OrderInfoVO orderInfoVO = orderInfoMapper.lookOrderInfoById(orderId);
        if(ObjectUtil.isEmpty(orderInfoVO)){
            Asserts.fail(ResultEnum.UNKNOWN_ERROR, "订单不存在");
        }
        Long id = orderService.getId();
        List<OrderServiceReceivableForm> orderServiceReceivableList = form.getOrderServiceReceivableList();//订单服务对应应收费用 list
        List<OrderServiceWithForm> orderServiceWithList = form.getOrderServiceWithList();//订单服务对应应付费用 list


        //1.保存订单服务
        this.saveOrUpdate(orderService);
        Long orderServiceId = orderService.getId();

        //2.保存订单服务对应应收费用
        List<Long> delOrderServiceReceivableIds = new ArrayList<>();//被删除的ids
        List<Long> reserveOrderServiceReceivableIds = new ArrayList<>();//被保留的ids
        List<OrderServiceReceivable> orderServiceReceivables = ConvertUtil.convertList(orderServiceReceivableList, OrderServiceReceivable.class);
        orderServiceReceivables.forEach(orderServiceReceivable -> {
            Long orderServiceReceivableId = orderServiceReceivable.getId();
            if(ObjectUtil.isNotEmpty(orderServiceReceivableId)){
                //被保留的id
                reserveOrderServiceReceivableIds.add(orderServiceReceivableId);
            }
            orderServiceReceivable.setQie(orderServiceId);//订单服务id(order_service id)
            //计算 总金额=数量 * 单价
            Integer c = orderServiceReceivable.getCount() == null ? 0 : orderServiceReceivable.getCount();//数量
            BigDecimal count = new BigDecimal(c.toString());
            BigDecimal unitPrice = orderServiceReceivable.getUnitPrice() == null ? new BigDecimal("0") : orderServiceReceivable.getUnitPrice();//单价
            BigDecimal amount = count.multiply(unitPrice);
            orderServiceReceivable.setAmount(amount);
        });
        delOrderServiceReceivableIds = orderServiceReceivableMapper.findOrderServiceReceivableByQieAndNotIds(orderServiceId, reserveOrderServiceReceivableIds);
        if(CollUtil.isNotEmpty(delOrderServiceReceivableIds)){
            //先删除 被删除的ids
            orderServiceReceivableService.removeByIds(delOrderServiceReceivableIds);
        }
        if(CollUtil.isNotEmpty(orderServiceReceivables)){
            //再批量保存
            orderServiceReceivableService.saveOrUpdateBatch(orderServiceReceivables);
        }

        //3.保存订单服务对应应付费用
        List<Long> delOrderServiceWithIds = new ArrayList<>();//被删除的ids
        List<Long> reserveOrderServiceWithIds = new ArrayList<>();//被保留的ids
        List<OrderServiceWith> orderServiceWiths = ConvertUtil.convertList(orderServiceWithList, OrderServiceWith.class);
        orderServiceWiths.forEach(orderServiceWith -> {
            Long orderServiceWithId = orderServiceWith.getId();
            if(ObjectUtil.isNotEmpty(orderServiceWithId)){
                //被保留的id
                reserveOrderServiceWithIds.add(orderServiceWithId);
            }
            orderServiceWith.setQie(orderServiceId);
            //计算 总金额=数量 * 单价
            Integer c = orderServiceWith.getCount() == null ? 0 : orderServiceWith.getCount();//数量
            BigDecimal count = new BigDecimal(c.toString());
            BigDecimal unitPrice = orderServiceWith.getUnitPrice() == null ? new BigDecimal("0") : orderServiceWith.getUnitPrice();//单价
            BigDecimal amount = count.multiply(unitPrice);
            orderServiceWith.setAmount(amount);
        });
        delOrderServiceWithIds = orderServiceWithMapper.findOrderServiceWithByQieAndNotIds(orderServiceId, reserveOrderServiceWithIds);
        if(CollUtil.isNotEmpty(delOrderServiceWithIds)){
            //先删除 被删除的ids
            orderServiceWithService.removeByIds(delOrderServiceWithIds);
        }
        if(CollUtil.isNotEmpty(orderServiceWiths)){
            //再批量保存
            orderServiceWithService.saveOrUpdateBatch(orderServiceWiths);
        }

        //4.保存订单服务对应应收费用 -对应关联的- 订单对应应收费用明细
        List<OrderServiceReceivableVO> orderServiceReceivableVOList = orderServiceReceivableMapper.findOrderServiceReceivableByQie(orderServiceId);
        List<OrderCopeReceivable> orderCopeReceivableList = new ArrayList<>();
        orderServiceReceivableVOList.forEach(orderServiceReceivableVO -> {
            Long orderServiceReceivableId = orderServiceReceivableVO.getId();//订单服务对应应收费用id(order_service_receivable id)
            OrderCopeReceivableVO orderCopeReceivableVO = orderCopeReceivableMapper.findOrderCopeReceivableByOrderServiceReceivableId(orderServiceReceivableId);
            OrderCopeReceivable orderCopeReceivable = new OrderCopeReceivable();
            if(ObjectUtil.isNotEmpty(orderCopeReceivableVO)){
                orderCopeReceivable = ConvertUtil.convert(orderCopeReceivableVO, OrderCopeReceivable.class);
            }
            orderCopeReceivable.setOrderId(orderId);
            orderCopeReceivable.setCostCode(orderServiceReceivableVO.getCostCode());
            orderCopeReceivable.setCostName(orderServiceReceivableVO.getCostName());
            orderCopeReceivable.setCalculateWay(orderServiceReceivableVO.getCalculateWay());
            orderCopeReceivable.setCount(new BigDecimal(orderServiceReceivableVO.getCount().toString()));
            orderCopeReceivable.setUnitPrice(orderServiceReceivableVO.getUnitPrice());
            orderCopeReceivable.setAmount(orderServiceReceivableVO.getAmount());
            orderCopeReceivable.setCid(orderServiceReceivableVO.getCid());
            orderCopeReceivable.setRemarks(orderServiceReceivableVO.getRemarks());
            orderCopeReceivable.setStatus(0);//状态(0未生成账单 1已生成账单)
            orderCopeReceivable.setOrderServiceReceivableId(orderServiceReceivableVO.getId());//订单服务对应应收费用id(order_service_receivable id)
            orderCopeReceivableList.add(orderCopeReceivable);
        });
        if(CollUtil.isNotEmpty(delOrderServiceReceivableIds)){
            QueryWrapper<OrderCopeReceivable> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            queryWrapper.in("order_service_receivable_id", delOrderServiceReceivableIds);
            //先删除 被移除的ids
            orderCopeReceivableService.remove(queryWrapper);
        }
        if(CollUtil.isNotEmpty(orderCopeReceivableList)){
            //在保存
            orderCopeReceivableService.saveOrUpdateBatch(orderCopeReceivableList);
        }

        //5.保存订单服务对应应付费用 -对应关联的- 订单对应应付费用明细
        List<OrderServiceWithVO> orderServiceWithVOList = orderServiceWithMapper.findOrderServiceWithByQie(orderServiceId);
        List<OrderCopeWith> orderCopeWiths = new ArrayList<>();
        orderServiceWithVOList.forEach(orderServiceWithVO -> {

            Long orderServiceWithId = orderServiceWithVO.getId();//订单服务对应应付费用id(order_service_with id)
            OrderCopeWithVO orderCopeWithVO = orderCopeWithMapper.findOrderCopeWithByOrderServiceWithId(orderServiceWithId);
            OrderCopeWith orderCopeWith = new OrderCopeWith();
            if(ObjectUtil.isNotEmpty(orderCopeWithVO)){
                orderCopeWith = ConvertUtil.convert(orderCopeWithVO, OrderCopeWith.class);
            }
            orderCopeWith.setOrderId(orderId);
            orderCopeWith.setCostCode(orderServiceWithVO.getCostCode());
            orderCopeWith.setCostName(orderServiceWithVO.getCostName());
            orderCopeWith.setSupplierId(orderServiceWithVO.getSupplierId());
            orderCopeWith.setCalculateWay(orderServiceWithVO.getCalculateWay());
            orderCopeWith.setCount(new BigDecimal(orderServiceWithVO.getCount().toString()));
            orderCopeWith.setUnitPrice(orderServiceWithVO.getUnitPrice());
            orderCopeWith.setAmount(orderServiceWithVO.getAmount());
            orderCopeWith.setCid(orderServiceWithVO.getCid());
            orderCopeWith.setRemarks(orderServiceWithVO.getRemarks());
            orderCopeWith.setStatus(0);//状态(0未生成账单 1已生成账单)
            orderCopeWith.setOrderServiceWithId(orderServiceWithVO.getId());//订单服务对应应付费用id(order_service_with id)
            orderCopeWiths.add(orderCopeWith);
        });
        if(CollUtil.isNotEmpty(delOrderServiceWithIds)){
            QueryWrapper<OrderCopeWith> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_id", orderId);
            queryWrapper.in("order_service_with_id", delOrderServiceWithIds);
            //先删除 被移除的ids
            orderCopeWithService.remove(queryWrapper);
        }
        if(CollUtil.isNotEmpty(orderCopeWiths)){
            //再保存
            orderCopeWithService.saveOrUpdateBatch(orderCopeWiths);
        }

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
