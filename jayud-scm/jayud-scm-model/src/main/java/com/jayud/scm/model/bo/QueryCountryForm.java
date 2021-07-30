package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCountryForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String name;

}
