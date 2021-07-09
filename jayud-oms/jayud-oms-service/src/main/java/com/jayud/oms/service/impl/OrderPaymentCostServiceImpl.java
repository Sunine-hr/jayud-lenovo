package com.jayud.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.oms.mapper.OrderPaymentCostMapper;
import com.jayud.oms.model.bo.GetCostDetailForm;
import com.jayud.oms.model.po.CurrencyInfo;
import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import com.jayud.oms.model.po.SupplierInfo;
import com.jayud.oms.model.vo.DriverOrderPaymentCostVO;
import com.jayud.oms.model.vo.InputPaymentCostVO;
import com.jayud.oms.service.ICurrencyInfoService;
import com.jayud.oms.service.IOrderPaymentCostService;
import com.jayud.oms.service.ISupplierInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
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

    @Autowired
    private ICurrencyInfoService currencyInfoService;
    @Autowired
    private ISupplierInfoService supplierInfoService;

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
            callbackParam.put(k, v);
        });
        return map;
    }


    /**
     * 查询待处理费用审核
     */
    @Override
    public List<Map<String, Object>> getPendingExpenseApproval(String subType, List<String> orderNos, List<Long> legalIds) {
        return this.baseMapper.getPendingExpenseApproval(subType, orderNos, legalIds);
    }

    @Override
    public List<OrderPaymentCost> getBySubType(String subType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("status", 2);
        queryWrapper.eq("sub_type", subType);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询应付供应商异常费用
     *
     * @param form
     * @return
     */
    @Override
    public List<InputPaymentCostVO> getSupplierAbnormalCostDetail(GetCostDetailForm form) {
        return this.baseMapper.getSupplierAbnormalCostDetail(form);
    }

    @Override
    public List<OrderPaymentCost> getByCondition(OrderPaymentCost paymentCost) {
        return this.baseMapper.selectList(new QueryWrapper<>(paymentCost));
    }

    /**
     * 根据子订单号集合查询供应商费用
     *
     * @param supplierId
     * @param subOrderNos
     * @return
     */
    @Override
    public List<OrderPaymentCost> getSupplierPayCostByOrderNos(Long supplierId,
                                                               List<String> subOrderNos,
                                                               Integer status) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().in(OrderPaymentCost::getOrderNo, subOrderNos)
                .eq(OrderPaymentCost::getSupplierId, supplierId);
        if (status != null) {
            condition.lambda().eq(OrderPaymentCost::getStatus, status);
        }
        return this.baseMapper.selectList(condition);
    }


    /**
     * 根据主订单号获取应收绑定数据
     *
     * @param paymentCost
     * @return
     */
    @Override
    public List<OrderPaymentCost> getReceivableBinding(OrderPaymentCost paymentCost) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderPaymentCost::getMainOrderNo, paymentCost.getMainOrderNo())
                .eq(OrderPaymentCost::getIsSumToMain, paymentCost.getIsSumToMain())
                .isNotNull(OrderPaymentCost::getReceivableId);
        return this.baseMapper.selectList(condition);
    }

    @Override
    public Map<String, Map<String, BigDecimal>> statisticalPayCostByOrderNos(List<OrderPaymentCost> list, Boolean isMain) {
        Map<String, List<OrderPaymentCost>> group = null;
        if (isMain) {
            group = list.stream().collect(Collectors.groupingBy(OrderPaymentCost::getMainOrderNo));
        } else {
            group = list.stream().collect(Collectors.groupingBy(OrderPaymentCost::getOrderNo));
        }
        List<CurrencyInfo> currencyInfos = currencyInfoService.list();
        Map<String, String> currencyMap = currencyInfos.stream().collect(Collectors.toMap(CurrencyInfo::getCurrencyCode, CurrencyInfo::getCurrencyName));
        Map<String, Map<String, BigDecimal>> map = new HashMap<>();
        group.forEach((k, v) -> {
            Map<String, BigDecimal> costMap = new HashMap<>();
            for (OrderPaymentCost tmp : v) {
                if (tmp.getAmount() == null || tmp.getChangeAmount() == null) return;
                costMap.merge(currencyMap.get(tmp.getCurrencyCode()), tmp.getAmount(), BigDecimal::add);
            }
            map.put(k, costMap);
        });
        return map;
    }

    /**
     * 获取主订单费用
     *
     * @param mainOrderNo
     * @return
     */
    @Override
    public List<OrderPaymentCost> getMainOrderCost(String mainOrderNo, List<String> mainCostStatus) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderPaymentCost::getMainOrderNo, mainOrderNo)
                .eq(OrderPaymentCost::getIsSumToMain, true)
                .and(e -> e.eq(OrderPaymentCost::getStatus, OrderStatusEnum.COST_3)//子订单合并费用
                        .or().in(OrderPaymentCost::getStatus, mainCostStatus));//主订单费用
        return this.baseMapper.selectList(condition);
    }

    /**
     * 根据主订单查询费用
     *
     * @param mainOrderNo
     * @param exclusionStatus
     * @return
     */
    @Override
    public List<OrderPaymentCost> getByMainOrderNo(String mainOrderNo, List<String> exclusionStatus) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().eq(OrderPaymentCost::getMainOrderNo, mainOrderNo)
                .notIn(OrderPaymentCost::getStatus, exclusionStatus);
        return this.baseMapper.selectList(condition);
    }

    /**
     * 补充供应商信息
     *
     * @param paymentCosts
     */
    @Override
    public void supplySupplierInfo(List<OrderPaymentCost> paymentCosts) {
        Map<String, String> supplierInfoMap = new HashMap<>();
        for (OrderPaymentCost paymentCost : paymentCosts) {
            //供应商名称
            if (StringUtils.isEmpty(paymentCost.getCustomerName())) {
                String supplierName = supplierInfoMap.get(paymentCost.getCustomerCode());
                if (com.jayud.common.utils.StringUtils.isEmpty(supplierName)) {
                    List<SupplierInfo> supplierInfos = this.supplierInfoService.getByCondition(new SupplierInfo().setSupplierCode(paymentCost.getCustomerCode()));
                    SupplierInfo supplierInfo = supplierInfos.get(0);
                    paymentCost.setCustomerName(supplierInfo.getSupplierChName());
                    supplierInfoMap.put(paymentCost.getCustomerCode(), supplierInfo.getSupplierChName());
                } else {
                    paymentCost.setCustomerName(supplierName);
                }
            }
        }

    }

    @Override
    public List<OrderPaymentCost> getSubCostByMainOrderNo(OrderPaymentCost paymentCost, List<String> exclusionStatus) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>(paymentCost);
        if (CollectionUtils.isNotEmpty(exclusionStatus)) {
            condition.lambda().notIn(OrderPaymentCost::getStatus, exclusionStatus);
        }
        return this.baseMapper.selectList(condition);
    }

    @Override
    public void updateByOrderNo(String mainOrderNo, String subOrder, OrderPaymentCost orderPaymentCost) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(mainOrderNo)) {
            condition.lambda().eq(OrderPaymentCost::getMainOrderNo, mainOrderNo);
        }
        if (StringUtils.isNotEmpty(subOrder)) {
            condition.lambda().eq(OrderPaymentCost::getOrderNo, subOrder);
        }
        this.update(orderPaymentCost, condition);
    }

    @Override
    public List<OrderPaymentCost> getOrderPaymentCostByMainOrderNos(List<String> mainOrderNos) {
        QueryWrapper<OrderPaymentCost> condition = new QueryWrapper<>();
        condition.lambda().in(OrderPaymentCost::getMainOrderNo, mainOrderNos);
        condition.lambda().in(OrderPaymentCost::getIsSumToMain, true);
        return this.baseMapper.selectList(condition);
    }

}
