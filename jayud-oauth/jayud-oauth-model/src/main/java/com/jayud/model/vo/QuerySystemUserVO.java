package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QuerySystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "联系方式")
    private String phone;

   @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "岗位")
    private String workName;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "所属公司")
    private String companyName;

    @ApiModelProperty(value = "所属上级")
    private String superiorName;

    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusDesc;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String strCreatedTime;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "岗位ID")
    private Long workId;

    @ApiModelProperty(value = "审核意见")
    private String note;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;


}
