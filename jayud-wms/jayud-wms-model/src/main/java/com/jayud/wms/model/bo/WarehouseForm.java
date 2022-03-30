package com.jayud.wms.model.bo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * WarehouseForm 实体类
 *
 * @author jyd
 * @since 2021-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WarehouseForm extends SysBaseEntity {


    @ApiModelProperty(value = "仓库编码")
    @NotNull(message = "仓库编码不为空")
    private String code;

    @ApiModelProperty(value = "仓库名称")
    @NotNull(message = "仓库名称不为空")
    private String name;

    @ApiModelProperty(value = "仓库类型(中文值)")
    @NotNull(message = "仓库类型不为空")
    private String type;

    @ApiModelProperty(value = "联系人")
    @NotNull(message = "联系人不为空")
    private String contacts;

    @ApiModelProperty(value = "手机号")
    @NotNull(message = "手机号不为空")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @NotNull(message = "邮箱不为空")
    private String email;

    @ApiModelProperty(value = "联系地址")
    @NotNull(message = "联系地址不为空")
    private String contactAddr;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    @NotNull(message = "状态不为空")
    private Integer status;

    @ApiModelProperty(value = "经度")
    private Double lo;

    @ApiModelProperty(value = "纬度")
    private Double la;

    @ApiModelProperty(value = "是否允许混放")
    private Boolean isMixing;

    @ApiModelProperty(value = "混放策略id(字典获取)")
    private Long mixingStrategyId;

    @ApiModelProperty(value = "混放策略(字典获取)")
    private String mixingStrategy;

    @ApiModelProperty(value = "上货策略id")
    private Long stockingStrategyId;

    @ApiModelProperty(value = "分配策略id(字典获取)")
    private Long allocationStrategyId;

    @ApiModelProperty(value = "分配策略(字典获取)")
    private String allocationStrategy;

    @ApiModelProperty(value = "周转方式(1先进先出,2后进先出)")
    private Integer turnoverMode;

    @ApiModelProperty(value = "周转批属性(1批次号,2生产日期,3字段1,4字段2,4字段3)")
    private Integer turnoverAttribute;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;






}
