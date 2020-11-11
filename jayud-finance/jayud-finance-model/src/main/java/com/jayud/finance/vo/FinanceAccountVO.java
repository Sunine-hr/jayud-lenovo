package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 财务核算列表
 */
@Data
public class FinanceAccountVO {

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "客户code")
    private String customerCode;

    @ApiModelProperty(value = "业务员")
    private String ywName;

    @ApiModelProperty(value = "应收账单号")
    private String sBillNo;

    @ApiModelProperty(value = "应收开始核算期")
    private String sBeginAccountTermStr;

    @ApiModelProperty(value = "应收结束核算期")
    private String sEndAccountTermStr;

    @ApiModelProperty(value = "应收人民币")
    private BigDecimal sRmb;

    @ApiModelProperty(value = "应收美元")
    private BigDecimal sDollar;

    @ApiModelProperty(value = "应收欧元")
    private BigDecimal sEuro;

    @ApiModelProperty(value = "应收港币")
    private BigDecimal sHkDollar;

    @ApiModelProperty(value = "应收本币金额")
    private BigDecimal sLocalAmount;

    @ApiModelProperty(value = "应收对账单状态")
    private String sStatus;

    @ApiModelProperty(value = "应收费用状态")
    private String sCostStatus;

    @ApiModelProperty(value = "应付账单号")
    private String fBillNo;

    @ApiModelProperty(value = "应付开始核算期")
    private String fBeginAccountTermStr;

    @ApiModelProperty(value = "应付结束核算期")
    private String fEndAccountTermStr;

    @ApiModelProperty(value = "应付人民币")
    private BigDecimal fRmb;

    @ApiModelProperty(value = "应付美元")
    private BigDecimal fDollar;

    @ApiModelProperty(value = "应付欧元")
    private BigDecimal fEuro;

    @ApiModelProperty(value = "应付港币")
    private BigDecimal fHkDollar;

    @ApiModelProperty(value = "应付本币金额")
    private BigDecimal fLocalAmount;

    @ApiModelProperty(value = "应付费用状态")
    private String fCostStatus;

    @ApiModelProperty(value = "应付对账单状态")
    private String fStatus;

    @ApiModelProperty(value = "利润(人民币)")
    private String profit;



}
