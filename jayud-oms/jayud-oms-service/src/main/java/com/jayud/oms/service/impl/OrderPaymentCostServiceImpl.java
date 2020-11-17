package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderPaymentCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.service.IOrderPaymentCostService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单对应应付费用明细 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderPaymentCostServiceImpl extends ServiceImpl<OrderPaymentCostMapper, OrderPaymentCost> implements IOrderPaymentCostService {

    @Override
    public List<InputPaymentCostVO> findPaymentCost(GetCostDetailForm form) {
        return baseMapper.findPaymentCost(form);
    }

    @Override
    public List<DriverOrderPaymentCostVO> getDriverOrderPaymentCost(String orderNo) {
        return this.baseMapper.getDriverOrderPaymentCost(orderNo);
    }

    /**
     * 判断是否司机已经提交费用
     */
    @Override
    public boolean isCostSubmitted(String orderNo) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderPaymentCost::getOrderNo, orderNo);
        return this.count(condition) > 0;
    }
}
