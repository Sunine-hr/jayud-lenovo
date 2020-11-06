package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class QueryNotPaidBillForm extends BasePageForm{

    @ApiModelProperty(value = "客户")
    private String customerName;

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

    @ApiModelProperty(value = "操作指令 cmd=main主订单操作 or zgys子订单操作 or bg子订单操作",required = true)
    @Pattern(regexp = "(main|zgys|bg)", message = "只允许填写main or zgys or bg ")
    private String cmd;
}
