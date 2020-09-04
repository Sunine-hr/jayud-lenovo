package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 角色权限管理-新增确认
 * @author chuanmei
 */
@Data
public class AddRoleForm {

    @ApiModelProperty(value = "角色名称", required = true)
    @NotEmpty(message = "角色名称不能为空")
    private String name;
    @ApiModelProperty(value = "角色描述")
    private String description;
    @ApiModelProperty(value = "该角色可查看的菜单id", required = true)
    @NotEmpty(message = "该角色可查看的菜单id不能为空")
    private List<Long> menuIds;
}
