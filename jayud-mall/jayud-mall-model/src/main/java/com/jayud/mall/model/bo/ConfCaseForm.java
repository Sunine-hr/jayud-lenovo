package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfCaseForm extends BasePageForm{

    @ApiModelProperty(value = "配载id")
    @NotNull(message = "配载id必填")
    private Long confId;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;
}
