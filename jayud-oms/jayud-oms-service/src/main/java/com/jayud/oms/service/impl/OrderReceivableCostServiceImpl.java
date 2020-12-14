package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.oms.mapper.OrderReceivableCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;
import com.jayud.oms.service.IOrderReceivableCostService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单对应应收费用明细 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-15
 */
@Service
public class OrderReceivableCostServiceImpl extends ServiceImpl<OrderReceivableCostMapper, OrderReceivableCost> implements IOrderReceivableCostService {

    @Override
    public List<InputReceivableCostVO> findReceivableCost(GetCostDetailForm form) {
        return baseMapper.findReceivableCost(form);
    }

    @Override
    public InputReceivableCostVO getWriteBackSCostData(Long costId) {
        return baseMapper.getWriteBackSCostData(costId);
    }

    /**
     * 获取审核通过费用
     */
    @Override
    public List<OrderReceivableCost> getApprovalFee(String mainOrder) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderReceivableCost::getMainOrderNo, mainOrder);
        condition.lambda().eq(OrderReceivableCost::getStatus, 3);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取审核通过费用
     */
    @Override
    public List<OrderReceivableCost> getApprovalFee(String mainOrder, List<Long> excludeIds) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderReceivableCost::getMainOrderNo, mainOrder);
        condition.lambda().eq(OrderReceivableCost::getStatus, 3);
        condition.lambda().notIn(OrderReceivableCost::getId, excludeIds);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 获取审核通过费用数目
     */
    @Override
    public Integer getApprovalFeeCount(String mainOrder) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderReceivableCost::getMainOrderNo, mainOrder);
        condition.lambda().eq(OrderReceivableCost::getStatus, 3);
        return this.count(condition);
    }

    /**
     * 获取审核通过费用数目
     */
    @Override
    public Integer getApprovalFeeCount(String mainOrder, List<Long> excludeIds) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderReceivableCost::getMainOrderNo, mainOrder);
        condition.lambda().eq(OrderReceivableCost::getStatus, 3);
        condition.lambda().notIn(OrderReceivableCost::getId, excludeIds);
        return this.count(condition);
    }
}
