package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色权限管理-查询
 * @author chuanmei
 */
@Data
public class QueryRoleForm extends BasePageForm{

    @ApiModelProperty(value = "角色名称")
    private String name;
}
