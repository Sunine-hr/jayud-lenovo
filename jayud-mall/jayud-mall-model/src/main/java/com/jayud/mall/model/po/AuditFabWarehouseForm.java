package com.jayud.mall.model.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditFabWarehouseForm {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "审核状态(0待审核 1审核通过 2审核不通过)")
    private Integer auditStatus;

}
