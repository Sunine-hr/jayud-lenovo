package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBillCostTotalVO {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "费用ID")
    private Long costId;

    @ApiModelProperty(value = "费用项")
    private String costInfoName;

    @ApiModelProperty(value = "录入费用时刻的本币金额")
    private BigDecimal oldLocalMoney;

    @ApiModelProperty(value = "出账本币汇率")
    private BigDecimal localMoneyRate;

    @ApiModelProperty(value = "录入费用时的金额")
    private BigDecimal money;

    @ApiModelProperty(value = "出账后的本币金额")
    private BigDecimal localMoney;

    @ApiModelProperty(value = "对账编号")
    private String billNo;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    public void setLocalMoneyRate(BigDecimal localMoneyRate) {
        this.localMoneyRate = localMoneyRate;
        this.localMoney = this.money.subtract(localMoneyRate);
    }


}
