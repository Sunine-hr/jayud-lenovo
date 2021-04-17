package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceForm {

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

}
