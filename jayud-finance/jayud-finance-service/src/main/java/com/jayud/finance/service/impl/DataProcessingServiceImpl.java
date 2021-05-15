package com.jayud.finance.service.impl;

import cn.hutool.core.map.MapUtil;
import com.jayud.common.utils.JDKUtils;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.po.CancelAfterVerification;
import com.jayud.finance.service.DataProcessingService;
import com.jayud.finance.service.ICancelAfterVerificationService;
import com.jayud.finance.service.IMakeInvoiceService;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.vo.InitComboxStrVO;
import com.jayud.finance.vo.OrderPaymentBillDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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
}
