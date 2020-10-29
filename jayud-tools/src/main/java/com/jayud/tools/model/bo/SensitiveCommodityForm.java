package com.jayud.tools.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SensitiveCommodityForm {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "品名")
    @NotEmpty(message = "敏感品名不能为空")
    private String name;

}
