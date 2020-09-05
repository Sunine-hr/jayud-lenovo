package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QueryOrgStructureVO {

    @ApiModelProperty(value = "部门主键ID")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String name;

}

