package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 应收出账单列表
 */
@Data
public class QueryReceiveBillForm extends BasePageForm{

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 " +
            "or bg子订单操作 or ky子订单操作",required = true)
    @Pattern(regexp = "(main|zgys|bg|ky|hy|nl|tc)", message = "只允许填写main or zgys or bg ")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户名",required = true)
    @NotEmpty(message = "legalName is required")
    private String loginUserName;

}
