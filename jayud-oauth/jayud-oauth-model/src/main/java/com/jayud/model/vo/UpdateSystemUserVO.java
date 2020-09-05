package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    @ApiModelProperty(value = "可开通账号的用户集合")
    List<QuerySystemUserNameVO> users;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "部门集合")
    private List<DepartmentVO> departments;

    @ApiModelProperty(value = "岗位集合")
    private List<WorkVO> works;

    @ApiModelProperty(value = "角色集合")
    private List<SystemRoleVO> roles;

    @ApiModelProperty(value = "所属公司集合")
    private List<CompanyVO> companys;

    @ApiModelProperty(value = "所属上级集合")
    private List<QuerySystemUserNameVO> superiors;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "岗位id")
    private Long workId;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "所属公司id")
    private Long companyId;

    @ApiModelProperty(value = "所属上级id")
    private Long superiorId;
}

