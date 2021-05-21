package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ServiceGroupIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "id不能为空")
    private Long id;

}
