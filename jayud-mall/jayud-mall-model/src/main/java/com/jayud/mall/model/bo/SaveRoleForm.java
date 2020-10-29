package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 保存角色表单
 */
@Data
public class SaveRoleForm {

    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称", required = true)
    @NotEmpty(message = "角色名称不能为空")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String roleDescribe;

    @ApiModelProperty(value = "该角色可查看的菜单id", required = true)
//    @NotEmpty(message = "该角色可查看的菜单id不能为空")
    private List<Long> menuIds;


}
