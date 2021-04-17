package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountVO {

    @ApiModelProperty(value = "金额", position = 1)
    @JSONField(ordinal = 1)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer cid;
}
