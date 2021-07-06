package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LegalEntityAuditForm {

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "审核状态 1-待审核 2-通过 0-拒绝")
    private Long auditStatus;


}
