package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mfc
 */
@Data
public class QueryRoleForm extends BasePageForm{

    @ApiModelProperty(value = "角色名称")
    private String roleName;


}
