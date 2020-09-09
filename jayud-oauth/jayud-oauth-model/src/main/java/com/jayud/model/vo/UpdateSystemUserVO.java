package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author bocong.zheng
 */
@Data
public class UpdateSystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "部门")
    private String departmentName;

    @ApiModelProperty(value = "岗位id")
    private Long workId;

    @ApiModelProperty(value = "岗位")
    private String workName;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "所属公司id")
    private Long companyId;

    @ApiModelProperty(value = "所属公司")
    private String companyName;

    @ApiModelProperty(value = "所属上级id")
    private Long superiorId;

    @ApiModelProperty(value = "所属上级")
    private String superiorName;
}

