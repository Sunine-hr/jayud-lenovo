package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class HandleSubProcessForm {

    @ApiModelProperty(value = "主订单ID",required = true)
    @NotNull(message = "mainOrderId is required")
    private Long mainOrderId;

    @ApiModelProperty(value = "产品类型",required = true)
    @NotEmpty(message = "classCode is required")
    private String classCode;

}
