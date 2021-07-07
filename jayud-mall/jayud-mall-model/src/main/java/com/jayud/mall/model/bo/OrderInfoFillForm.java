package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderInfoFillForm {


    @ApiModelProperty(value = "自增id")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "补充资料说明")
    @NotBlank(message = "补充资料说明不能为空")
    private String fillMaterialDescription;


}
