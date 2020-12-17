package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台用户表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemUser对象", description="后台用户表")
public class SystemUser extends Model<SystemUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "1-是 0-否", position = 8)
    private String isDepartmentCharge;

    @ApiModelProperty(value = "所属公司(直接输入名称)", position = 12)
    private String company;

    @ApiModelProperty(value = "最后登录时间", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "帐号启用状态：0->Off；1->On", position = 15)
    private Integer status;

    @ApiModelProperty(value = "1-待审核 2-审核通过 0-审核拒绝", position = 16)
    private String auditStatus;

    @ApiModelProperty(value = "备注", position = 18)
    private String note;

    @ApiModelProperty(value = "创建人", position = 19)
    private Integer createdUser;

    @ApiModelProperty(value = "创建时间", position = 20)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改时间", position = 21)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "修改人", position = 22)
    private Integer updatedUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
