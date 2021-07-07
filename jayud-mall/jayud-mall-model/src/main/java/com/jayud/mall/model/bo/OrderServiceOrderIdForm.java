package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderServiceOrderIdForm {

    @ApiModelProperty(value = "订单id(order_info id)")
    @NotNull(message = "订单id不能为空")
    private Long orderId;


}
