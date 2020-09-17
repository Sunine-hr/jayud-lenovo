package com.jayud.oauth.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author bocong.zheng
 */
@Data
public class SystemUserVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID")
    private Long workId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "所属公司ID")
    private Long companyId;

    @ApiModelProperty(value = "所属上级ID")
    private Long superiorId;

    @ApiModelProperty(value = "备注信息")
    private String note;

    @ApiModelProperty(value = "最后登录时间")
    private String loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off；1->On")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private String createdTime;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "用户token")
    private String token;

}
