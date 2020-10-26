package com.jayud.tms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RejectOrderForm {

    @ApiModelProperty(value = "子订单ID",required = true)
    @NotNull(message = "orderId is required")
    private Long orderId;

    @ApiModelProperty(value = "操作指令",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

}
