package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 保存角色表单
 */
@Data
public class SaveRoleForm {

    @ApiModelProperty(value = "角色ID", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "角色名称", required = true, position = 2)
    @NotBlank(message = "角色名称不能为空")
    @JSONField(ordinal = 2)
    private String roleName;

    @ApiModelProperty(value = "角色描述", position = 3)
    @JSONField(ordinal = 3)
    private String roleDescribe;

    @ApiModelProperty(value = "该角色可查看的菜单id", required = true, position = 4)
    @NotEmpty(message = "菜单id不能为空")
    @JSONField(ordinal = 4)
    private List<Long> menuIds;


}
