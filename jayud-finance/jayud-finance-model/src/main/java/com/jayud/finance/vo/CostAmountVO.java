package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 开票和付款申请/开票和付款核销/核销界面展示的金额
 */
@Data
public class CostAmountVO {

    //以下是应收
    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "开票申请,核销,开票核销:应收金额或申请开票金额")//开票申请,核销,开票核销
    private BigDecimal ysAmount;

    @ApiModelProperty(value = "开票申请,核销,开票核销:应收金额或申请开票金额对应的币种")//开票申请,核销,开票核销
    private String ysCurrency;

    @ApiModelProperty(value = "应收核销:未收金额")//核销
    private BigDecimal wsAmount;

    @ApiModelProperty(value = "应收核销:未收金额对应的币种")//核销
    private String wsCurrency;

    //以下是应付
    @ApiModelProperty(value = "付款申请,核销,付款核销:应付金额")//付款申请,核销,付款核销
    private BigDecimal yfAmount;

    @ApiModelProperty(value = "付款申请,核销,付款核销:应付金额对应的币种")//付款申请,核销,付款核销
    private String yfCurrency;

    @ApiModelProperty(value = "应付核销:待付金额")//核销
    private BigDecimal dfAmount;

    @ApiModelProperty(value = "应付核销:待付金额对应的币种")//核销
    private String dfCurrency;




}
