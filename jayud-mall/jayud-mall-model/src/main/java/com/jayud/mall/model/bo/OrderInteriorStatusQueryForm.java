package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderInteriorStatusQueryForm {

    @ApiModelProperty(value = "订单id(order_info id)")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "内部状态唯一码")
    @NotBlank(message = "内部状态唯一码不能为空")
    private String interiorStatusCode;

}
