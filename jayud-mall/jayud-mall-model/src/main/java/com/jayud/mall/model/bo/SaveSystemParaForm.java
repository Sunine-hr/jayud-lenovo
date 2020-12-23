package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveSystemParaForm {

    @ApiModelProperty(value = "角色ID", position = 1)
    private Long id;
}
