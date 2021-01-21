package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
public class QueryPaymentBillNumForm{

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "供应商",required = true)
    @NotEmpty(message = "supplierChName is required")
    private String supplierChName;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作",required = true)
    @Pattern(regexp = "(main|zgys|bg)", message = "只允许填写main or zgys or bg ")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户名",required = true)
    @NotEmpty(message = "legalName is required")
    private String loginUserName;

}
