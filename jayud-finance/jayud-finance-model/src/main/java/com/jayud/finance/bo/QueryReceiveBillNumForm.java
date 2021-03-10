package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
public class QueryReceiveBillNumForm {

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "结算单位",required = true)
    @NotEmpty(message = "unitAccount is required")
    private String unitAccount;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作",required = true)
    @Pattern(regexp = "(main|zgys|bg|ky|hy|nl)", message = "只允许填写main or zgys or bg or ky or nl")
    private String cmd;

}
