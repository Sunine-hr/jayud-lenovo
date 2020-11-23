package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddDriverOrderTransportForm {


    @ApiModelProperty(value = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    @NotEmpty(message = "订单编号不能为空")
    private String orderNo;

}
