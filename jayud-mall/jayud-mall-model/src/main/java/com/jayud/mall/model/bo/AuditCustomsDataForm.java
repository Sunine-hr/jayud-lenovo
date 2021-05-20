package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuditCustomsDataForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "审核状态(0待审核 1已审核 2已取消)")
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;
}
