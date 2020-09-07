package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 审核客户信息界面
 */
@Data
public class AuditCustomerInfoForm {

    @ApiModelProperty(value = "主键",required = true)
    @NotEmpty(message = "id is required")
    private Integer id;

    @ApiModelProperty(value = "审核角色",required = true)
    @NotEmpty(message = "roleFlag is required")
    @Pattern(regexp = "kf|cw|zjb",message = "types requires 'kf' or 'cw' or 'zjb' only")
    private String roleFlag;

    @ApiModelProperty(value = "审核状态 0-拒绝 1-通过",required = true)
    @NotEmpty(message = "auditStatus is required")
    @Pattern(regexp = "0|1",message = "auditStatus requires '0' or '1' only")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;
}
