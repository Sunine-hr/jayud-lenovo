package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SaveSystemParaForm {

    @ApiModelProperty(value = "角色ID", position = 1)
    @NotNull(message = "角色ID不能为空")
    private Long id;
}
