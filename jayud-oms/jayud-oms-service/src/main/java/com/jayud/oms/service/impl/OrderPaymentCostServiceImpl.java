package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderPaymentCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.service.IOrderPaymentCostService;
import org.apache.commons.collections4.CollectionUtils;
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

    @Override
    public InputPaymentCostVO getWriteBackFCostData(Long costId) {
        return baseMapper.getWriteBackFCostData(costId);
    }

    /**
     * 根据主订单号/子订单号批量获取提交审核通过费用
     */
    @Override
    public List<OrderPaymentCost> getCostSubmittedByOrderNos(List<String> mainOrders,
                                                             List<String> subOrderNos) {
        if (CollectionUtils.isNotEmpty(mainOrders)){

        }else {

        }

        return null;
    }

}
