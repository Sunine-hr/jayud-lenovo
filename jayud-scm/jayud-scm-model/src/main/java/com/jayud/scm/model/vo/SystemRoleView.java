package com.jayud.scm.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chuanmei
 */
@Data
public class SystemRoleView {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "授权人员")
    private String roleUser;

}
