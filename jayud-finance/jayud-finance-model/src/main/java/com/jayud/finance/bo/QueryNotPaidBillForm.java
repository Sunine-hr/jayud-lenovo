package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
public class QueryNotPaidBillForm extends BasePageForm {

    @ApiModelProperty(value = "供应商,应付必填")
    private String supplierChName;

    @ApiModelProperty(value = "结算单位,应收必填")
    private String unitAccount;

    @ApiModelProperty(value = "法人主体", required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "创建开始日期")
    private String beginCreatedTimeStr;

    @ApiModelProperty(value = "创建结束日期")
    private String endCreatedTimeStr;

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作", required = true)
    @Pattern(regexp = "(main|zgys|bg|ky|hy|nl)", message = "只允许填写main or zgys or bg ")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户名",required = true)
    @NotEmpty(message = "legalName is required")
    private String loginUserName;
}
