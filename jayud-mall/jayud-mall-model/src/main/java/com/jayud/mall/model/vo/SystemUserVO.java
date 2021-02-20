package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mfc
 * @description:
 * @date 2020/10/23 16:05
 */
@Data
public class SystemUserVO {


    @ApiModelProperty(value = "主键ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "登录名,用户名", position = 2)
    private String name;

    @ApiModelProperty(value = "密码", position = 3)
    private String password;

    @ApiModelProperty(value = "用户姓名", position = 4)
    private String userName;

    @ApiModelProperty(value = "英文名", position = 5)
    private String enUserName;

    @ApiModelProperty(value = "手机号(联系方式)", position = 6)
    private String phone;

    @ApiModelProperty(value = "邮箱", position = 7)
    private String email;

    @ApiModelProperty(value = "所属公司(直接输入名称)", position = 8)
    private String company;

    @ApiModelProperty(value = "工号", position = 9)
    private String wkno;

    @ApiModelProperty(value = "最后登录时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off 启用；1->On 停用", position = 11)
    private Integer status;

    @ApiModelProperty(value = "备注", position = 12)
    private String note;

    @ApiModelProperty(value = "创建人(system_user id)", position = 13)
    private Integer createdUser;

    @ApiModelProperty(value = "创建时间", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改人(system_user id)", position = 15)
    private Integer updatedUser;

    @ApiModelProperty(value = "修改时间", position = 16)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "用户的角色", position = 17)
    private List<SystemRoleVO> systemRoleVOS;
    

}
