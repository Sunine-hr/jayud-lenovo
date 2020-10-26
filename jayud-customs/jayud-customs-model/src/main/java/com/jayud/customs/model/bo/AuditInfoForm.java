package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditInfoForm {

    @ApiModelProperty(value = "被审核表的ID")
    private Long extId;

    @ApiModelProperty(value = "审核类别描述")
    private String auditTypeDesc;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "被审核表的ID备注")
    private String extDesc;


}
