package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CounterListInfoIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "自增id")
    private Long id;

}
