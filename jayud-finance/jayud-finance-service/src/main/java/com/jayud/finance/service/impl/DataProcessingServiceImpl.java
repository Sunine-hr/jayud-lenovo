package com.jayud.finance.service.impl;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.utils.JDKUtils;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBill;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.vo.FinanceAccountVO;
import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 数据处理
 */
@Service
public class DataProcessingServiceImpl implements DataProcessingService {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IOrderBillCostTotalService costTotalService;
    @Autowired
    private IMakeInvoiceService makeInvoiceService;
    @Autowired
    private ICancelAfterVerificationService cancelAfterVerificationService;
    @Autowired
    private IOrderPaymentBillDetailService paymentBillDetailService;
    @Autowired
    private IOrderReceivableBillDetailService receivableBillDetailService;

    /**
     * 应收/应付对账单列表,应收/应付对账单审核列表,应收/应付财务应付对账单列表
     */
    @Override
    public void processingPaymentBillDetail(List<OrderPaymentBillDetailVO> list, Integer type) {
        List<String> billNos = list.stream().map(OrderPaymentBillDetailVO::getBillNo).collect(toList());
        //统计合计币种金额
        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(billNos);
        //审核意见
        Map<String, Object> auditComment = this.omsClient.getByExtUniqueFlag(billNos).getData();
        //币种
        List<InitComboxStrVO> currencyInfo = omsClient.initCurrencyInfo().getData();
        Map<String, String> currencyInfoMap = currencyInfo.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));
        //核销金额
        Map<String, BigDecimal> heXiaoAmountMap = this.makeInvoiceService.calculationAccounting(billNos, 2);
        //核销用户
        List<CancelAfterVerification> verifications = cancelAfterVerificationService.getByBillNos(billNos);
        Map<String, List<CancelAfterVerification>> verificationMap = JDKUtils.getGroupMapByLastData(verifications,
                CancelAfterVerification::getCreatedTime,
                CancelAfterVerification::getBillNo);
        list.forEach(e -> {
            BigDecimal money = heXiaoAmountMap.get(e.getBillNo()) == null ? new BigDecimal(0) : heXiaoAmountMap.get(e.getBillNo());
            e.setHeXiaoAmount(money).setNotHeXiaoAmount(e.getPaymentAmount().subtract(money))
                    .setAuditComment(MapUtil.getStr(auditComment, e.getBillNo()))
                    .setSettlementCurrency(currencyInfoMap.get(e.getSettlementCurrency()))
                    .totalCurrencyAmount(currencyAmounts)
                    .assemblyVerificationInfo(verificationMap);
        });
    }


    /**
     * 财务核算数据处理
     */
    @Override
    public void processingFinanceAccount(List<FinanceAccountVO> list, Integer type) {
        List<String> mainOrders = list.stream().map(FinanceAccountVO::getOrderNo).collect(toList());

        Set<String> billNos = new HashSet<>();

        QueryWrapper<OrderPaymentBillDetail> condition = new QueryWrapper<>();
        condition.lambda().select(OrderPaymentBillDetail::getBillNo)
                .in(OrderPaymentBillDetail::getOrderNo, mainOrders);
        List<OrderPaymentBillDetail> payBills = this.paymentBillDetailService.list(condition);

        QueryWrapper<OrderReceivableBillDetail> reCondition = new QueryWrapper<>();
        reCondition.lambda().select(OrderReceivableBillDetail::getBillNo)
                .in(OrderReceivableBillDetail::getOrderNo, mainOrders);
        List<OrderReceivableBillDetail> receivableBills = this.receivableBillDetailService.list(reCondition);

        if (CollectionUtils.isNotEmpty(payBills)) {
            billNos.addAll(payBills.stream().map(OrderPaymentBillDetail::getBillNo).collect(Collectors.toList()));
        }
        if (CollectionUtils.isNotEmpty(receivableBills)) {
            billNos.addAll(receivableBills.stream().map(OrderReceivableBillDetail::getBillNo).collect(Collectors.toList()));
        }

        //统计合计币种金额
        List<Map<String, Object>> currencyAmounts = this.costTotalService.totalCurrencyAmount(new ArrayList<>(billNos));

        //币种
//        List<InitComboxStrVO> currencyInfo = omsClient.initCurrencyInfo().getData();
//        Map<String, String> currencyInfoMap = currencyInfo.stream().collect(Collectors.toMap(e -> e.getCode(), e -> e.getName()));

        list.forEach(e -> {
            e.totalCurrencyAmount(currencyAmounts);
        });
    }
}
