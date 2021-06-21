package com.jayud.finance.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.ApiResult;
import com.jayud.common.UserOperator;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.VoidBillForm;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.po.*;
import com.jayud.finance.mapper.VoidBillingRecordsMapper;
import com.jayud.finance.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 作废账单表 服务实现类
 * </p>
 *
 * @author chuanmei
 * @since 2021-06-21
 */
@Service
@Slf4j
public class VoidBillingRecordsServiceImpl extends ServiceImpl<VoidBillingRecordsMapper, VoidBillingRecords> implements IVoidBillingRecordsService {

    @Autowired
    private IOrderReceivableBillDetailService receivableBillDetailService;
    @Autowired
    private IOrderPaymentBillDetailService paymentBillDetailService;
    @Autowired
    private IOrderBillCostTotalService billCostTotalService;
    @Autowired
    private IOrderPaymentBillService paymentBillService;
    @Autowired
    private IOrderReceivableBillService receivableBillService;
    @Autowired
    private OmsClient omsClient;

    /**
     * 作废账单
     *
     * @param form
     */
    @Override
    @Transactional
    public void voidBill(VoidBillForm form) {

        //1-应付 2-应收
        String moneyType = form.getCostType().equals(0) ? "2" : "1";
        //根据账单编号查询费用明细统计表
        List<OrderBillCostTotal> orderBillCostTotals = this.billCostTotalService.getByBillNo(Arrays.asList(form.getBillNo()), moneyType);
        //根据costId查询应收/应付费用明细
        //根据costType分别做操作
        //删除账单详情
        //删除费用维度统计表
        //重新计算账单金额
        //修改费用状态(已审核,未出账单)
        //修改失败捕获并且抛出异常执行回滚操作
    }


    /**
     * 作废账单
     *
     * @param billNo
     * @param type   0-应收,1-应付
     */
    @Override
    @Transactional
    public void voidBill(String billNo, Integer type, Integer costStatus) {
        //查询账单明细
        List<VoidBillingRecords> voidBillingRecords = this.assembleBillDetail(billNo, type);
        //根据costId查询录用应收/应付费用
        List<Long> costIds = this.assembleCost(voidBillingRecords, type);
        //费用统计维度
        this.assembleBillCostTotal(voidBillingRecords, billNo, type);
        LocalDateTime now = LocalDateTime.now();
        voidBillingRecords.forEach(e -> e.setOperator(UserOperator.getToken()).setOperationTime(now));
        //增加作废记录
        this.saveBatch(voidBillingRecords);
        //清理账单数据
        this.cleaningUpBillingData(billNo, voidBillingRecords.get(0).getBillId(),type, costStatus, costIds);
    }

    /**
     * 清理账单数据
     *  @param billNo
     * @param billId
     * @param type       0-应收,1-应付
     * @param costStatus
     * @param costIds
     */
    @Override
    @Transactional
    public void cleaningUpBillingData(String billNo, Long billId, Integer type, Integer costStatus, List<Long> costIds) {
        QueryWrapper condition = new QueryWrapper();
        condition.eq("bill_no", billNo);
        //1-应付 2-应收
        String moneyType = type.equals(0) ? "2" : "1";
        //根据type分别做操作
        //删除账单详情,删除费用维度统计表,重新计算账单金额
        if (BillTypeEnum.RECEIVABLE.getCode().equals(type)) {
            this.receivableBillDetailService.getBaseMapper().delete(condition);
            condition.eq("money_type", moneyType);
            this.billCostTotalService.getBaseMapper().delete(condition);
            this.receivableBillService.statisticsBill(billId);
        } else {
            this.receivableBillDetailService.getBaseMapper().delete(condition);
            condition.eq("money_type", moneyType);
            this.billCostTotalService.getBaseMapper().delete(condition);
            this.paymentBillService.statisticsBill(billId);
        }

        //修改费用状态(已审核,未出账单)
        try {
            ApiResult result = this.omsClient.batchUpdateCostStatus(costIds, "0", costStatus, type);
            if (!result.isOk()) throw new JayudBizException("作废订单操作失败");
        } catch (Exception e) {
            //修改失败捕获并且抛出异常执行回滚操作
            log.error("作废订单操作失败 msg={}", e.getMessage());
            throw new JayudBizException("作废订单操作失败");
        }
    }

    /**
     * 组装费用统计维度
     *
     * @param voidBillingRecords
     * @param billNo
     * @param type
     */
    private void assembleBillCostTotal(List<VoidBillingRecords> voidBillingRecords, String billNo, Integer type) {

        //1-应付 2-应收
        String moneyType = type.equals(0) ? "2" : "1";
        //根据账单编号查询费用明细统计表
        List<OrderBillCostTotal> orderBillCostTotals = this.billCostTotalService.getByBillNo(Collections.singletonList(billNo), moneyType);
        voidBillingRecords.forEach(e -> {
            for (OrderBillCostTotal orderBillCostTotal : orderBillCostTotals) {
                if (e.getBillNo().equals(orderBillCostTotal.getBillNo())
                        && e.getCostId().equals(orderBillCostTotal.getCostId())) {
                    e.setCostInfoName(orderBillCostTotal.getCostInfoName()).setMoney(orderBillCostTotal.getMoney())
                            .setLocalMoneyRate(orderBillCostTotal.getLocalMoneyRate())
                            .setCostType(type.toString()).setSettlementCurrency(orderBillCostTotal.getCurrencyCode())
                            .setCurrentCurrencyCode(orderBillCostTotal.getCurrentCurrencyCode())
                            .setExchangeRate(orderBillCostTotal.getExchangeRate()).setIsCustomExchangeRate(orderBillCostTotal.getIsCustomExchangeRate());
                }
            }
        });
    }


    /**
     * 组装费用(应收/应付)
     *
     * @param voidBillingRecords
     * @param type
     */
    private List<Long> assembleCost(List<VoidBillingRecords> voidBillingRecords, Integer type) {
        Map<Long, VoidBillingRecords> map = voidBillingRecords.stream().collect(Collectors.toMap(VoidBillingRecords::getCostId, e -> e));

        Long billId = voidBillingRecords.get(0).getBillId();
        String legalName;
        if (BillTypeEnum.RECEIVABLE.getCode().equals(type)) {
            OrderReceivableBill bill = this.receivableBillService.getById(billId);
            legalName = bill.getLegalName();
        } else {
            OrderPaymentBill bill = this.paymentBillService.getById(billId);
            legalName = bill.getLegalName();
        }

        Object data = this.omsClient.getCostInfo(new ArrayList<>(map.keySet()), type).getData();
        JSONArray costList = new JSONArray(data);
        List<Long> costIds = new ArrayList<>();
        for (int i = 0; i < costList.size(); i++) {
            JSONObject cost = costList.getJSONObject(i);
            VoidBillingRecords tmp = map.get(cost.getLong("id"));
            tmp.setMainOrderNo(cost.getStr("mainOrderNo"));
            tmp.setOrderNo(cost.getStr("orderNo"));
            tmp.setLegalName(legalName);
            tmp.setCustomerName(cost.getStr("customerName"));
            tmp.setSubType(cost.getStr("subType"));
            tmp.setIsSumToMain(cost.getBool("isSumToMain"));
            tmp.setAmount(cost.getBigDecimal("amount"));
            costIds.add(tmp.getCostId());
        }

        return costIds;
    }

    /**
     * 组装账单明细
     *
     * @param billNo
     * @param type
     * @return
     */
    private List<VoidBillingRecords> assembleBillDetail(String billNo, Integer type) {
        List<VoidBillingRecords> voidBillingRecords;
        if (BillTypeEnum.RECEIVABLE.getCode().equals(type)) {
            List<OrderReceivableBillDetail> tmp = this.receivableBillDetailService.getByCondition(new OrderReceivableBillDetail().setBillNo(billNo));
            voidBillingRecords = ConvertUtil.convertList(tmp, VoidBillingRecords.class);
        } else {
            List<OrderPaymentBillDetail> tmp = this.paymentBillDetailService.getByCondition(new OrderPaymentBillDetail().setBillNo(billNo));
            voidBillingRecords = ConvertUtil.convertList(tmp, VoidBillingRecords.class);
        }
        return voidBillingRecords;
    }


}
