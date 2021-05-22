package com.jayud.tms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class QueryOrderTmsForm extends BasePageForm {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "通关口岸code,下拉框隐藏值")
    private String portCode;

    @ApiModelProperty(value = "开始创建时间")
    private String beginCreatedTime;

    @ApiModelProperty(value = "结束创建时间")
    private String endCreatedTime;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "当前登录用户,前台传")
    private String loginUserName;

    @ApiModelProperty(value = "当前用户账户类型(1-用户 2-客户 3-供应商)")
    private String accountType;

    @ApiModelProperty(value = "操作指令", required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;


}
