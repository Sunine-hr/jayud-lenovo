package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "客户ID(customer id)", position = 2)
    @JSONField(ordinal = 2)
    private Long customerId;

    @ApiModelProperty(value = "币种(currency_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Long cid;

    @ApiModelProperty(value = "金额", position = 4)
    @JSONField(ordinal = 4)
    private BigDecimal amount;

    //扩展显示字段
    @ApiModelProperty(value = "客户名称(customer company)", position = 5)
    @JSONField(ordinal = 5)
    private String customerName;

    @ApiModelProperty(value = "币种代码", position = 6)
    @JSONField(ordinal = 6)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 7)
    @JSONField(ordinal = 7)
    private String currencyName;

    @ApiModelProperty(value = "账户余额格式化(金额 + 币种名称)", position = 8)
    @JSONField(ordinal = 8)
    private String amountFormat;


}
