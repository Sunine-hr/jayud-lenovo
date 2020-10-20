package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class ConfirmChangeStatusForm {

    @ApiModelProperty(value = "子订单号",required = true)
    @NotEmpty(message = "orderNo is required")
    private String orderNo;

    @ApiModelProperty(value = "订单类型",required = true)
    @NotEmpty(message = "orderType is required")
    private String orderType;


}
