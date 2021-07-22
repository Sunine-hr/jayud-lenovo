package com.jayud.finance.vo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.CancelAfterVerification;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 应付/应收一致对账单
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderPaymentBillDetailVO {

    @ApiModelProperty(value = "主键用于排序")
    private Long id;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户,应收时取值")
    private String unitAccount;

    @ApiModelProperty(value = "供应商,应付时取值")
    private String supplierChName;

    @ApiModelProperty(value = "核算期 年月")
    private String accountTermStr;

    @ApiModelProperty(value = "人民币")
    private BigDecimal rmb;

    @ApiModelProperty(value = "美元")
    private BigDecimal dollar;

    @ApiModelProperty(value = "欧元")
    private BigDecimal euro;

    @ApiModelProperty(value = "港币")
    private BigDecimal hKDollar;

    @ApiModelProperty(value = "核销金额(已收金额)")
    private BigDecimal heXiaoAmount;

    @ApiModelProperty(value = "未核销金额(未收金额)")
    private BigDecimal notHeXiaoAmount;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusDesc;

    @ApiModelProperty(value = "付款申请")
    private String applyStatus;

    @ApiModelProperty(value = "生成账单人")
    private String makeUser;

    @ApiModelProperty(value = "生成账单时间")
    private String makeTimeStr;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private String auditTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "核销人")
    private String heXiaoUser;

    @ApiModelProperty(value = "核销时间")
    private String heXiaoTimeStr;

    @ApiModelProperty(value = "推金蝶次数")
    private Integer pushKingdeeCount;

    @ApiModelProperty(value = "(应付:付款金额,应收:开票金额)")
    private BigDecimal paymentAmount = new BigDecimal(0);

    @ApiModelProperty(value = "费用金额")
    private String amountStr;

    @ApiModelProperty(value = "费用类型(0-应收, 1-应付)")
    private Integer costType;

    public String getAuditStatusDesc() {
        if (!StringUtil.isNullOrEmpty(this.auditStatus)) {
            return BillEnum.getDesc(this.auditStatus);
        }
        return "";
    }

    public String getApplyStatus() {
        if (!StringUtil.isNullOrEmpty(this.applyStatus)) {
            return BillEnum.getDesc(this.applyStatus);
        }
        return "";
    }


    public OrderPaymentBillDetailVO totalCurrencyAmount(List<Map<String, Object>> currencyAmounts, Map<String, String> currencyInfo) {
        for (Map<String, Object> currencyAmount : currencyAmounts) {
            if (!MapUtil.getStr(currencyAmount, "billNo").equals(this.billNo)) {
                continue;
            }
//            String key = "amount";
//            if ("CNY".equals(currencyAmount.get("currencyCode"))) {
//                this.rmb = (BigDecimal) currencyAmount.get(key);
//            }
//            if ("USD".equals(currencyAmount.get("currencyCode"))) {
//                this.dollar = (BigDecimal) currencyAmount.get(key);
//            }
//            if ("EUR".equals(currencyAmount.get("currencyCode"))) {
//                this.euro = (BigDecimal) currencyAmount.get(key);
//            }
//            if ("HKD".equals(currencyAmount.get("currencyCode"))) {
//                this.hKDollar = (BigDecimal) currencyAmount.get(key);
//            }

            StringBuilder sb = new StringBuilder();
            String currencyName = currencyInfo.get(currencyAmount.get("currencyCode").toString());
            sb.append(currencyAmount.get("amount")).append(" ").append(currencyName);
            this.amountStr=sb.toString();
        }
        return this;
    }


    public OrderPaymentBillDetailVO totalCurrencyAmount(List<Map<String, Object>> currencyAmounts) {
        for (Map<String, Object> currencyAmount : currencyAmounts) {
            if (!MapUtil.getStr(currencyAmount, "billNo").equals(this.billNo)) {
                continue;
            }
            String key = "amount";
            if ("CNY".equals(currencyAmount.get("currencyCode"))) {
                this.rmb = (BigDecimal) currencyAmount.get(key);
            }
            if ("USD".equals(currencyAmount.get("currencyCode"))) {
                this.dollar = (BigDecimal) currencyAmount.get(key);
            }
            if ("EUR".equals(currencyAmount.get("currencyCode"))) {
                this.euro = (BigDecimal) currencyAmount.get(key);
            }
            if ("HKD".equals(currencyAmount.get("currencyCode"))) {
                this.hKDollar = (BigDecimal) currencyAmount.get(key);
            }
        }
        return this;
    }

    public OrderPaymentBillDetailVO assemblyVerificationInfo(Map<String, List<CancelAfterVerification>> verificationMap) {
        if (CollectionUtil.isEmpty(verificationMap) || CollectionUtil.isEmpty(verificationMap.get(this.billNo))) {
            return this;
        }
        CancelAfterVerification verification = verificationMap.get(this.billNo).get(0);
        this.heXiaoUser = verification.getCreatedUser();
        this.heXiaoTimeStr = DateUtils.LocalDateTime2Str(verification.getRealReceiveTime(), DateUtils.DATE_PATTERN);
        return this;
    }
}
