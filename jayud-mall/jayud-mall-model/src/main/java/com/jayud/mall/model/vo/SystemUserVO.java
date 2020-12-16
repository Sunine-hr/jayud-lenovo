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


    @ApiModelProperty(value = "主键ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "登录名", position = 2)
    private String name;

    @ApiModelProperty(value = "密码", position = 3)
    private String password;

    @ApiModelProperty(value = "用户姓名", position = 4)
    private String userName;

    @ApiModelProperty(value = "英文名", position = 5)
    private String enUserName;

    @ApiModelProperty(value = "联系方式", position = 6)
    private String phone;

    @ApiModelProperty(value = "邮箱", position = 7)
    private String email;

    @ApiModelProperty(value = "1-是 0-否", position = 8)
    private String isDepartmentCharge;

    @ApiModelProperty(value = "部门id", position = 9)
    private Long departmentId;

    @ApiModelProperty(value = "岗位ID", position = 10)
    private Long workId;

    @ApiModelProperty(value = "岗位描述", position = 11)
    private String workName;

    @ApiModelProperty(value = "所属公司id", position = 12)
    private Long companyId;

    @ApiModelProperty(value = "所属上级id", position = 13)
    private Long superiorId;

    @ApiModelProperty(value = "最后登录时间", position = 14)
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off 关闭；1->On 启用", position = 15)
    private Integer status;

    @ApiModelProperty(value = "1-待审核 2-审核通过 0-审核拒绝", position = 16)
    private String auditStatus;

    @ApiModelProperty(value = "1-用户 2-客户", position = 17)
    private String userType;

    @ApiModelProperty(value = "备注", position = 18)
    private String note;

    @ApiModelProperty(value = "创建人", position = 19)
    private String createdUser;

    @ApiModelProperty(value = "创建时间", position = 20)
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改时间", position = 21)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改人", position = 22)
    private String updatedUser;

    

}
