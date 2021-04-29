package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.mapper.OrderPaymentCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.service.IOrderPaymentCostService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (CollectionUtils.isNotEmpty(mainOrders)) {

        } else {

        }

        return null;
    }

    @Override
    public boolean isCost(String orderNo, Integer type) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        if (type == 1) {
            condition.lambda().eq(OrderPaymentCost::getOrderNo, orderNo);
        }
        return this.count(condition) > 0;
    }

    /**
     * 根据类型查询费用详情
     *
     * @param subType 0.主订单,1子订单
     * @return
     */
    @Override
    public List<OrderPaymentCost> getByType(List<String> orderNos, String subType) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        if (SubOrderSignEnum.MAIN.getSignOne().equals(subType)) {
            condition.lambda().in(OrderPaymentCost::getMainOrderNo, orderNos)
                    .eq(OrderPaymentCost::getSubType, subType);
        } else {
            condition.lambda().in(OrderPaymentCost::getOrderNo, orderNos)
                    .eq(OrderPaymentCost::getSubType, subType);
        }


        return this.baseMapper.selectList(condition);
    }


    @Override
    public Map<String, Object> getOrderCostStatus(List<String> mainOrderNos, List<String> subOrderNos,
                                                  Map<String, Object> callbackParam) {

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
        List<OrderPaymentCost> paymentCosts = null;
        Map<String, List<OrderPaymentCost>> group = null;

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(mainOrderNos)) {
            QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
            condition.lambda().in(OrderPaymentCost::getMainOrderNo, mainOrderNos)
                    .eq(OrderPaymentCost::getIsSumToMain, true);
//                    .eq(OrderReceivableCost::getSubType, SubOrderSignEnum.MAIN.getSignOne());
            paymentCosts = this.baseMapper.selectList(condition);

            //过滤子订单审核状态
            paymentCosts = paymentCosts.stream()
                    .filter(e -> SubOrderSignEnum.MAIN.getSignOne().equals(e.getSubType())
                            || (StringUtils.isNotEmpty(e.getOrderNo()) && OrderStatusEnum.COST_3.getCode().equals(e.getStatus().toString())))
                    .collect(Collectors.toList());

            //主单分组
            group = paymentCosts.stream().collect(Collectors.groupingBy(OrderPaymentCost::getMainOrderNo));
        }
        if (CollectionUtils.isNotEmpty(subOrderNos)) {
            QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
            condition.lambda().in(OrderPaymentCost::getOrderNo, subOrderNos);

            paymentCosts = this.baseMapper.selectList(condition);
            //子订单分组
            group = paymentCosts.stream().collect(Collectors.groupingBy(OrderPaymentCost::getOrderNo));
        }

        Map<String, Object> map = new HashMap<>();
        if (group == null) {
            return map;
        }

        group.forEach((k, v) -> {
            int submited = 0;
            int audited = 0;
            String str = "";
            BigDecimal approvedAmount = new BigDecimal(0);
            for (OrderPaymentCost paymentCost : v) {
                String status = String.valueOf(paymentCost.getStatus());
                if (OrderStatusEnum.COST_0.getCode().equals(status) ||
                        OrderStatusEnum.COST_1.getCode().equals(status)) {
                    str = "已录入-" + approvedAmount;
                }
                if (OrderStatusEnum.COST_2.getCode().equals(status)) {
                    ++submited;
                }
                if (OrderStatusEnum.COST_3.getCode().equals(status)) {
                    ++audited;
                    approvedAmount = approvedAmount.add(paymentCost.getChangeAmount());
                }
            }
            if (submited > 0 && str.length() == 0) {
                map.put(k, "已提交-" + approvedAmount);
            } else if (audited > 0 && str.length() == 0) {
                map.put(k, "已审核-" + approvedAmount);
            } else if (str.length() == 0) {
                map.put(k, "未录入-" + approvedAmount);
            } else {
                map.put(k, str);
            }
            //回滚参数
            callbackParam.put(k,v);
        });
        return map;
    }

}
