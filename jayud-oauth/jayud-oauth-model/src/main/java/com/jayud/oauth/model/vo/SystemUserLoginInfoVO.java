package com.jayud.oauth.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SystemUserLoginInfoVO {


    @ApiModelProperty(value = "用户角色")
    private List<SystemRoleVO> roles;

    @ApiModelProperty(value = "角色ID集合")
    private List<Long> roleIds;

    @ApiModelProperty(value = "菜单树")
    private List<SystemMenuNode> menuNodeList;


}
