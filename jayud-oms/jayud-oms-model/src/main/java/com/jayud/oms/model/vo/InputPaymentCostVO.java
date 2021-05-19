package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class InputPaymentCostVO {

    @ApiModelProperty(value = "应付主键ID,修改时传")
    private Long id;

    @ApiModelProperty(value = "主订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "应付客户CODE")
    private String customerCode;

    @ApiModelProperty(value = "应付客户名称")
    private String customerName;

    @ApiModelProperty(value = "费用类别ID")
    private Long costTypeId;

    @ApiModelProperty(value = "费用类别")
    private String costType;

    @ApiModelProperty(value = "费用类型ID")
    private Long costGenreId;

    @ApiModelProperty(value = "费用类型")
    private String costGenre;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "应付项目CODE")
    private String costCode;

    @ApiModelProperty(value = "应付项目")
    private String costName;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "数量")
    private BigDecimal number;

    @ApiModelProperty(value = "币种CODE")
    private String currencyCode;

    @ApiModelProperty(value = "币种")
    private String currencyName;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "原始币种,财务审核通过后反推数据到费用录入时用于校验")
    private String oCurrencyName;

    @ApiModelProperty(value = "卸货地址")
    private String unloadingAddress;

}
