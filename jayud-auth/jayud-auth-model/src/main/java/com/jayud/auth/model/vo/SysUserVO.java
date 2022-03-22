package com.jayud.auth.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * SysUser 实体类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "后台用户表对象", description = "后台用户表")
public class SysUserVO extends SysBaseEntity {


    @ApiModelProperty(value = "登录名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

    @ApiModelProperty(value = "工号")
    private String numberName;

    @ApiModelProperty(value = "用户图像")
    private String headPortrait;

    @ApiModelProperty(value = "性别(0-默认未知,1-男,2-女)")
    private Integer sex;

    @ApiModelProperty(value = "电话号码")
    private String phone;

    @ApiModelProperty(value = "手机号码")
    private String cellPhoneNumber;

    @ApiModelProperty(value = "钉钉")
    private String nailingNumber;

    @ApiModelProperty(value = "微信号码")
    private String wechatNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "是否部门负责人1-是 0-否")
    private Boolean isDepartCharge;

    @ApiModelProperty(value = "所属部门id")
    private Long departId;

    @ApiModelProperty(value = "所属部门id节点")
    private String departmentList;


    @ApiModelProperty(value = "负责部门ids")
    private String departIds;

    @ApiModelProperty(value = "多租户标识")
    private String relTenantIds;

    @ApiModelProperty(value = "岗位ID")
    private Long postId;

    @ApiModelProperty(value = "入职时间")
    private Date entryTime;

    @ApiModelProperty(value = "员工状态：0-离职，1-在职")
    private Integer jobStatus;

    @ApiModelProperty(value = "状态(1:在职,0:离职)")
    private String statusDesc;

    @ApiModelProperty(value = "冻结状态：0-冻结，1-正常")
    private Integer status;

    @ApiModelProperty(value = "1-用户 2-客户 3-供应商")
    private String userType;

    @ApiModelProperty(value = "上司id")
    private Long supervisorId;

    @ApiModelProperty(value = "兼职部门(多个逗号隔开)")
    private String partTimeDepId;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "用户关联的角色")
    private String roleListIdString;

    @ApiModelProperty(value = "角色id集合")
    private List<Long> roleIds;


    @ApiModelProperty(value = "所属部门")
    private String departIdName;

    @ApiModelProperty(value = "岗位名称")
    private String postIdName;

    @ApiModelProperty(value = "上司id对应的名字")
    private String supervisorIdName;

    @ApiModelProperty(value = "所属部门id节点集合")
    private List<Long> departIdLists;


    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
        this.statusDesc = this.jobStatus == 1 ? "在职" : "离职";
    }
}
