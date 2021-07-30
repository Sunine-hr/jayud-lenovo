package com.jayud.scm.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QueryMenuStructureVO {

    @ApiModelProperty(value = "菜单ID")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String label;

    @ApiModelProperty(value = "父级菜单ID")
    private Long fId;

    private List<QueryMenuStructureVO> children;

}

