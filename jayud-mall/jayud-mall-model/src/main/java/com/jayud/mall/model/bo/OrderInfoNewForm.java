package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderInfoNewForm {

    @ApiModelProperty(value = "订单号", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "报价id(offer_info id)", position = 2)
    @JSONField(ordinal = 2)
    @NotNull(message = "报价id不能为空")
    private Integer offerInfoId;

}
