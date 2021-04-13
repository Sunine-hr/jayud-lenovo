package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.oms.mapper.OrderReceivableCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;
import com.jayud.oms.service.IOrderReceivableCostService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    /**
     * 是否录用过费用
     *
     * @param orderNo
     * @param type    0.主订单,1子订单
     * @return
     */
    @Override
    public boolean isCost(String orderNo, Integer type) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        if (type == 1) {
            condition.lambda().eq(OrderReceivableCost::getOrderNo, orderNo);
        }
        return this.count(condition) > 0;
    }

    @Override
    public Map<String, Object> getOrderCostStatus(List<String> mainOrderNo, List<String> subOrderNo) {

        //根据子订单查询费用
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        condition.lambda().in(OrderReceivableCost::getOrderNo, subOrderNo);

        List<OrderReceivableCost> orderReceivableCosts = this.baseMapper.selectList(condition);
        //分组
        Map<String, List<OrderReceivableCost>> group = orderReceivableCosts.stream().collect(Collectors.groupingBy(OrderReceivableCost::getOrderNo));

        Map<String, Object> map = new HashMap<>();
        group.forEach((k, v) -> {
            int submited = 0;
            int audited = 0;
            String str = "";
            for (OrderReceivableCost orderReceivableCost : v) {
                if (OrderStatusEnum.COST_0.getCode().equals(orderReceivableCost.getStatus()) ||
                        OrderStatusEnum.COST_1.getCode().equals(orderReceivableCost.getStatus())) {
                    str = "已提交";
                    break;
                }
                if (OrderStatusEnum.COST_2.getCode().equals(orderReceivableCost.getStatus())) {
                    ++submited;
                }
                if (OrderStatusEnum.COST_3.getCode().equals(orderReceivableCost.getStatus())) {
                    ++audited;
                }
            }

            if (submited > 0 && str.length() == 0) {
                map.put(k, "已提交");
            }else if (audited > 0 && str.length() == 0) {
                map.put(k, "已提交");
            }else if (str.length()==0){
                map.put(k,"未提交");
            }

        });


        //已录单:当数据库存在费用,并且金额不为0状态,就为已录单状态

        //已提交:例如5条费用都是已提交状态才是已提交状态,当只有4条是已提交,还有一条待提交,那么就是已录单

        //已审核:5条费用都审核才是已审核,


        //多种场景

        //注意只要一笔暂存费用,都是已录入状态,不管你已提交和审核多少条费用都是已录用

        //需要全部费用都是已提交的状态,才是已提交

        //需要全部费用都审核通过才是审核状态

        return null;
    }
}
