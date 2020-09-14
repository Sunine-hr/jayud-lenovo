package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class EditRoleMenuVO {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "仅供前端使用")
    private String webFlag;

    @ApiModelProperty(value = "菜单树")
    private List<Long> menuIds;

}

