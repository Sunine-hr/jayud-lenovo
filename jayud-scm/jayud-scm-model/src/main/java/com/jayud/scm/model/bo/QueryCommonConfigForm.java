package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QueryCommonConfigForm extends BasePageForm{

    @ApiModelProperty(value = "SQL代码")
    @NotBlank(message = "SQL代码，不能为空")
    private String sqlCode;
}
