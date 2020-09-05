package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditLegalEntityForm {

    @ApiModelProperty(value = "法人主体ID 修改时必传",required = true)
    private Long id;

    @ApiModelProperty(value = "审核状态 1-待审核 2-通过 3-拒绝")
    private Long auditStatus;
}
