package com.jayud.tms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.StringUtils;
import com.jayud.tms.mapper.OrderTransportMapper;
import com.jayud.tms.model.bo.InputOrderTakeAdrForm;
import com.jayud.tms.model.bo.InputOrderTransportForm;
import com.jayud.tms.model.po.DeliveryAddress;
import com.jayud.tms.model.po.OrderTakeAdr;
import com.jayud.tms.model.po.OrderTransport;
import com.jayud.tms.service.IDeliveryAddressService;
import com.jayud.tms.service.IOrderTakeAdrService;
import com.jayud.tms.service.IOrderTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 中港运输订单 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderTransportServiceImpl extends ServiceImpl<OrderTransportMapper, OrderTransport> implements IOrderTransportService {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    IOrderTakeAdrService orderTakeAdrService;

    @Autowired
    IDeliveryAddressService deliveryAddressService;

    @Autowired
    IOrderTransportService orderTransportService;


    @Override
    public boolean createOrderTransport(InputOrderTransportForm form) {
        OrderTransport orderTransport = ConvertUtil.convert(form,OrderTransport.class);
        if(orderTransport == null){
            return false;
        }
        List<InputOrderTakeAdrForm> orderTakeAdrForms1 = form.getOrderTakeAdrForms1();
        List<InputOrderTakeAdrForm> orderTakeAdrForms2 = form.getOrderTakeAdrForms2();
        List<InputOrderTakeAdrForm> orderTakeAdrForms = form.getOrderTakeAdrForms2();
        orderTakeAdrForms.addAll(orderTakeAdrForms1);
        orderTakeAdrForms.addAll(orderTakeAdrForms2);
        if(orderTransport.getId() != null){//修改
            //修改时,先把以前的收货提货地址清空
            List<Long> ids = new ArrayList<>();
            for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
               Long deliveryId = inputOrderTakeAdrForm.getDeliveryId();
               ids.add(deliveryId);
            }
            deliveryAddressService.removeByIds(ids);//删除地址信息
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("order_no",form.getOrderNo());
            orderTakeAdrService.remove(queryWrapper);//删除货物信息
            orderTransport.setUpdatedTime(LocalDateTime.now());
            orderTransport.setUpdatedUser(getLoginUser());
        }else {//新增
            //生成订单号
            String orderNo = StringUtils.loadNum(CommonConstant.T,12);
            while (true){
                if(!isExistOrder(orderNo)){//重复
                    orderNo = StringUtils.loadNum(CommonConstant.T,12);
                }else {
                    break;
                }
            }
            orderTransport.setOrderNo(orderNo);
            orderTransport.setCreatedUser(getLoginUser());
        }
        for (InputOrderTakeAdrForm inputOrderTakeAdrForm : orderTakeAdrForms) {
            OrderTakeAdr orderTakeAdr = ConvertUtil.convert(inputOrderTakeAdrForm,OrderTakeAdr.class);
            DeliveryAddress deliveryAddress = ConvertUtil.convert(inputOrderTakeAdrForm,DeliveryAddress.class);
            deliveryAddressService.save(deliveryAddress);
            orderTakeAdr.setDeliveryId(deliveryAddress.getId());
            orderTakeAdrService.save(orderTakeAdr);
        }
        boolean result = orderTransportService.saveOrUpdate(orderTransport);
        return result;
    }

    @Override
    public boolean isExistOrder(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(SqlConstant.ORDER_NO,orderNo);
        List<OrderTransport> orderTransports = baseMapper.selectList(queryWrapper);
        if(orderTransports == null || orderTransports.size() == 0){
            return true;
        }
        return false;
    }

    /**
     * 当前登录用户
     * @return
     */
    private String getLoginUser(){
        return redisUtils.get("loginUser",100);
    }
}
