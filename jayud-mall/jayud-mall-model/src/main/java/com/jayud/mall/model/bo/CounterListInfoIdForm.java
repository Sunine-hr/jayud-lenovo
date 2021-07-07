package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(value = "CounterListInfoIdForm", description = "柜子清单")
@Data
public class CounterListInfoIdForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "自增id")
    private Long id;

}
