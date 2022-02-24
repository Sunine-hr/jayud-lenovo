package com.jayud.auth.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * SysDepart 实体类
 *
 * @author jayud
 * @since 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="组织机构表对象", description="组织机构表")
public class SysDepart extends SysBaseEntity {


    @ApiModelProperty(value = "父机构ID")
    private Long parentId;

    @ApiModelProperty(value = "机构/部门名称")
    private String departName;

    @ApiModelProperty(value = "英文名")
    private String departNameEn;

    @ApiModelProperty(value = "缩写")
    private String departNameAbbr;

    @ApiModelProperty(value = "排序")
    private Integer departOrder;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "机构类别 1集团，2公司，3部门")
    private String orgCategory;

    @ApiModelProperty(value = "机构编码")
    private String orgCode;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "状态（1启用，0不启用）")
    private Boolean status;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "组织/部门负责人")
    private Long principalId;

    @ApiModelProperty(value = "给前端展示")
    @TableField(exist = false)
    private List<SysDepart> children;


    @ApiModelProperty(value = "给前端展示")
    @TableField(exist = false)
    private String label;

    @ApiModelProperty(value = "给前端展示")
    @TableField(exist = false)
    private Long value;

    @ApiModelProperty(value = "给前端展示")
    @TableField(exist = false)
    private List<Long> parentIdList;

    @ApiModelProperty(value = "组织/部门负责人-给前端展示")
    @TableField(exist = false)
    private String principalName;

    @ApiModelProperty(value = "员工id")
    @TableField(exist = false)
    private Long userId;

    @ApiModelProperty(value = "员工管理")
    private List<SysUser> sysUsersList;
}
