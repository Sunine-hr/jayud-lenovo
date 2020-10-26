package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RejectOrderForm {

    @ApiModelProperty(value = "主订单ID",required = true)
    @NotEmpty(message = "mainOrderNo is required")
    private String mainOrderNo;

    @ApiModelProperty(value = "操作指令",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

}
