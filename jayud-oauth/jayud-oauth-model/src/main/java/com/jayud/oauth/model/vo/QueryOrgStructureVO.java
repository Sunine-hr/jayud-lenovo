package com.jayud.oauth.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class QueryOrgStructureVO {

    @ApiModelProperty(value = "部门主键ID")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String label;

    @ApiModelProperty(value = "父级部门ID")
    private Long fId;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    private List<QueryOrgStructureVO> children;

}

