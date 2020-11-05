package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author mfc
 * @description:
 * @date 2020/10/23 16:05
 */
@Data
public class SystemUserVO {


    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "联系方式")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "1-是 0-否")
    private String isDepartmentCharge;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID")
    private Long workId;

    @ApiModelProperty(value = "岗位描述")
    private String workName;

    @ApiModelProperty(value = "所属公司id")
    private Long companyId;

    @ApiModelProperty(value = "所属上级id")
    private Long superiorId;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off；1->On")
    private Integer status;

    @ApiModelProperty(value = "1-待审核 2-审核通过 0-审核拒绝")
    private String auditStatus;

    @ApiModelProperty(value = "1-用户 2-客户")
    private String userType;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    

}
