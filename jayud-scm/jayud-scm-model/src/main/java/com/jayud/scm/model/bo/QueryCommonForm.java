package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QueryCommonForm extends BasePageForm{

    @ApiModelProperty("搜索条件")
    private String name;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("下一步操作")
    private Integer next;

    @ApiModelProperty("权限code")
    private String actionCode;

    @ApiModelProperty("id集合")
    private List<Integer> ids;

}
