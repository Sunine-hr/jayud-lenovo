package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBillCostTotalVO {


    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "费用ID")
    private Long costId;

    @ApiModelProperty(value = "费用项")
    private String costInfoName;

    @ApiModelProperty(value = "录入费用时刻的本币金额")
    private BigDecimal oldMoney;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "对账编号")
    private String billNo;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;


}
