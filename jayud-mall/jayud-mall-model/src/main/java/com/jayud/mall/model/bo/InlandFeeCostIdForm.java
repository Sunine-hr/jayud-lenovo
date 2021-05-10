package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InlandFeeCostIdForm {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "主键id不能为空")
    private Long id;

}
