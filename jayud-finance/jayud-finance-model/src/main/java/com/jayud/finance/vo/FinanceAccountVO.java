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
    private String recBillNo;

    @ApiModelProperty(value = "应收核算期")
    private String recAccountTermStr;

    @ApiModelProperty(value = "应收人民币")
    private BigDecimal recRmb;

    @ApiModelProperty(value = "应收美元")
    private BigDecimal recDollar;

    @ApiModelProperty(value = "应收欧元")
    private BigDecimal recEuro;

    @ApiModelProperty(value = "应收港币")
    private BigDecimal recHkDollar;

    @ApiModelProperty(value = "应收本币金额")
    private BigDecimal recLocalAmount;

    @ApiModelProperty(value = "应收对账单状态")
    private String recStatus;

    @ApiModelProperty(value = "应收费用状态")
    private String recCostStatus;

    @ApiModelProperty(value = "应付账单号")
    private String payBillNo;

    @ApiModelProperty(value = "应付核算期")
    private String payAccountTermStr;

    @ApiModelProperty(value = "应付人民币")
    private BigDecimal payRmb;

    @ApiModelProperty(value = "应付美元")
    private BigDecimal payDollar;

    @ApiModelProperty(value = "应付欧元")
    private BigDecimal payEuro;

    @ApiModelProperty(value = "应付港币")
    private BigDecimal payHkDollar;

    @ApiModelProperty(value = "应付本币金额")
    private BigDecimal payLocalAmount;

    @ApiModelProperty(value = "应付费用状态")
    private String payCostStatus;

    @ApiModelProperty(value = "应付对账单状态")
    private String payStatus;

    @ApiModelProperty(value = "利润(人民币)")
    private String profit;



}
