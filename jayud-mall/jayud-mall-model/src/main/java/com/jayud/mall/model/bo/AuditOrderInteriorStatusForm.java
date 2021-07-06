package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AuditOrderInteriorStatusForm {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "状态标志")
    @NotBlank(message = "状态标志")
    private String statusFlag;

}
