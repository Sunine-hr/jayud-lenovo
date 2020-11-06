package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class QueryPaymentBillForm extends BasePageForm{

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作",required = true)
    @Pattern(regexp = "(main|zgys|bg)", message = "只允许填写main or zgys or bg ")
    private String cmd;

}
