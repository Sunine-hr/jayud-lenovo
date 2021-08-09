package com.jayud.scm.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String condition;

    @ApiModelProperty("搜索key")
    private String key;

    @ApiModelProperty("模糊条件")
    private String name;


}
