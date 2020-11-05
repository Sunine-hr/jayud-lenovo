package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author mfc
 */
@Data
public class SystemRoleVO {

    @ApiModelProperty(value = "主键ID")
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDescribe;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "该角色所拥有的菜单Ids", required = true)
    private List<Long> menuIds;


}
