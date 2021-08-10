package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCommonForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String name;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("权限code")
    private String actionCode;

}
