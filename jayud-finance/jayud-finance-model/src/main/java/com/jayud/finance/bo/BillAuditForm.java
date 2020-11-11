package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 对账单审核
 */
@Data
public class BillAuditForm {

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotNull(message = "billDetailId is required")
    private Long billDetailId;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核状态",required = true)
    @Pattern(regexp = "(0|1)", message = "只允许填写0-通过 1-不通过")
    private String auditStatus;


}
