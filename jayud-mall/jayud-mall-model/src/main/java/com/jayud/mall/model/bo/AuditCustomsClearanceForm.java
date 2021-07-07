package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditCustomsClearanceForm {

    @ApiModelProperty(value = "主键id，自增")
    private Long id;

    @ApiModelProperty(value = "审核状态(0待审核 1已审核 2已取消)")
    private Integer auditStatus;

}
