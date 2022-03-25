package com.jayud.auth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionForm {

    @ApiModelProperty(value = "权限Code", required = true)
    private String actionCode;


}
