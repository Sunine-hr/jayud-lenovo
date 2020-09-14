package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class AuditSystemUserForm {

    @ApiModelProperty(value = "主键ID",required = true)
    @NotEmpty(message = "主键ID不能为空")
    private Long id;

    @ApiModelProperty(value = "审核意见")
    @NotEmpty(message = "审核意见不能为空")
    private String note;

    @ApiModelProperty(value = "审核状态 2-通过 0-拒绝",required = true)
    @NotEmpty(message = "审核状态不能为空")
    @Pattern(regexp = "2|0",message = "auditStatus requires '2' or '0' only")
    private String auditStatus;




}
