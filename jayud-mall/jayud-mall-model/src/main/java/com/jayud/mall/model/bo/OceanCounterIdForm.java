package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OceanCounterIdForm {

    @ApiModelProperty(value = "自增加ID", position = 1)
    @NotNull(message = "自增id不能为空")
    private Long id;

}
