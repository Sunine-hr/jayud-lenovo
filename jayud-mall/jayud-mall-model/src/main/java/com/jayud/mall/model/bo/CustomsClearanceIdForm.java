package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomsClearanceIdForm {

    @ApiModelProperty(value = "主键id，自增")
    @NotNull(message = "id不能为空")
    private Long id;

}
