package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class QueryCommonConfigForm extends BasePageForm{

    @ApiModelProperty(value = "SQL代码")
    @NotBlank(message = "SQL代码，不能为空")
    private String sqlCode;


    @ApiModelProperty(value = "条件参数{k-v},键值对")
    private Map<String, Object> condPara;
}
