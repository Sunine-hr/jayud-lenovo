package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 对账单审核
 */
@Data
public class BillAuditForm {

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核状态 0-通过 1-不通过",required = true)
    @Pattern(regexp = "(0|1)", message = "只允许填写0-通过 1-不通过")
    private String auditStatus;

    @ApiModelProperty(value = "操作指令 cmd = audit or cw_audit",required = true)
    @Pattern(regexp = "(audit|cw_audit)", message = "只允许填写audit or cw_audit")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户",required = true)
    private String loginUserName;


}
