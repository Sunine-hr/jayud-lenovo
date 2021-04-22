package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderInfoParaForm {

    @ApiModelProperty(value = "订单id", position = 1, required = true)
    @JSONField(ordinal = 1)
    @NotNull(message = "订单id不能为空")
    private Long id;

}
