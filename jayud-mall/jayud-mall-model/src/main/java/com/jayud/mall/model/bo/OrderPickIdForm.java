package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderPickIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "提货id不能为空")
    private Long id;

}
