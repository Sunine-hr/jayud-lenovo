package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.mapper.OrderReceivableCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.vo.InputReceivableCostVO;
import com.jayud.oms.service.IOrderReceivableCostService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
    public Map<String, Object> getOrderCostStatus(List<String> mainOrderNos, List<String> subOrderNos) {

        /**
         * 已录单:当数据库存在费用,并且金额不为0状态,就为已录单状态
         * 已提交:例如5条费用都是已提交状态才是已提交状态,当只有4条是已提交,还有一条待提交,那么就是已录单
         * 已审核:5条费用都审核才是已审核
         * 注意:
         * 只要一笔暂存费用,都是已录入状态,不管你已提交和审核多少条费用都是已录用
         * 需要全部费用都是已提交的状态,才是已提交
         * 需要全部费用都审核通过才是审核状态
         */

        //根据子订单查询费用
        List<OrderReceivableCost> orderReceivableCosts = null;
        Map<String, List<OrderReceivableCost>> group = null;
        if (CollectionUtils.isNotEmpty(mainOrderNos)) {
            QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
            condition.lambda().in(OrderReceivableCost::getMainOrderNo, mainOrderNos)
                    .eq(OrderReceivableCost::getIsSumToMain, true);
//                    .eq(OrderReceivableCost::getSubType, SubOrderSignEnum.MAIN.getSignOne());
            orderReceivableCosts = this.baseMapper.selectList(condition);

            //过滤子订单审核状态
            orderReceivableCosts = orderReceivableCosts.stream()
                    .filter(e -> SubOrderSignEnum.MAIN.getSignOne().equals(e.getSubType())
                            || (StringUtils.isNotEmpty(e.getOrderNo()) && OrderStatusEnum.COST_3.getCode().equals(e.getStatus().toString())))
                    .collect(Collectors.toList());

            //子订单审核通过
//            condition = new QueryWrapper<>();
//            condition.lambda().in(OrderReceivableCost::getMainOrderNo, mainOrderNos)
//                    .eq(OrderReceivableCost::getIsSumToMain, true)
//                    .isNotNull(OrderReceivableCost::getOrderNo)
//                    .eq(OrderReceivableCost::getStatus, OrderStatusEnum.COST_3.getCode());
//            List<OrderReceivableCost> subOrderReceivableCosts = this.baseMapper.selectList(condition);
//            orderReceivableCosts.addAll(subOrderReceivableCosts);
            //主单分组
            group = orderReceivableCosts.stream().collect(Collectors.groupingBy(OrderReceivableCost::getMainOrderNo));
        }
        if (CollectionUtils.isNotEmpty(subOrderNos)) {
            QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
            condition.lambda().in(OrderReceivableCost::getOrderNo, subOrderNos);

            orderReceivableCosts = this.baseMapper.selectList(condition);
            //子订单分组
            group = orderReceivableCosts.stream().collect(Collectors.groupingBy(OrderReceivableCost::getOrderNo));
        }

        Map<String, Object> map = new HashMap<>();
        if (group == null) {
            return map;
        }

        group.forEach((k, v) -> {
            int submited = 0;
            int audited = 0;
            String str = "";
            for (OrderReceivableCost orderReceivableCost : v) {
                String status = String.valueOf(orderReceivableCost.getStatus());
                if (OrderStatusEnum.COST_0.getCode().equals(status) ||
                        OrderStatusEnum.COST_1.getCode().equals(status)) {
                    str = "已录单";
                    break;
                }
                if (OrderStatusEnum.COST_2.getCode().equals(status)) {
                    ++submited;
                }
                if (OrderStatusEnum.COST_3.getCode().equals(status)) {
                    ++audited;
                }
            }

            if (submited > 0 && str.length() == 0) {
                map.put(k, "已提交");
            } else if (audited > 0 && str.length() == 0) {
                map.put(k, "已审核");
            } else if (str.length() == 0) {
                map.put(k, "未录单");
            } else {
                map.put(k, str);
            }

        });
        return map;
    }

    @Override
    public List<OrderReceivableCost> getByType(List<String> orderNos, String subType) {
        QueryWrapper<OrderReceivableCost> condition = new QueryWrapper<>();
        if (SubOrderSignEnum.MAIN.getSignOne().equals(subType)) {
            condition.lambda().in(OrderReceivableCost::getMainOrderNo, orderNos)
                    .eq(OrderReceivableCost::getSubType, subType);
        } else {
            condition.lambda().in(OrderReceivableCost::getOrderNo, orderNos)
                    .eq(OrderReceivableCost::getSubType, subType);
        }

        return this.baseMapper.selectList(condition);
    }
}
