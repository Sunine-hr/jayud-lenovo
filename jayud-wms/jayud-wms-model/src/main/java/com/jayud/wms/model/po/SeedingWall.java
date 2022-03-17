package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SeedingWall 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SeedingWall对象", description="播种墙信息")
@TableName(value = "wms_seeding_wall")
public class SeedingWall extends SysBaseEntity {

    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "播种墙编号")
    private String code;

    @ApiModelProperty(value = "播种墙名称")
    @TableField("`name`")
    private String name;

    @ApiModelProperty(value = "分播类型id(字典类型获取)")
    @TableField("`type`")
    private Long type;

    @ApiModelProperty(value = "分播类型")
    private String typeDesc;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "行")
    @TableField("`row`")
    private Integer row;

    @ApiModelProperty(value = "列")
    @TableField("`column`")
    private Integer column;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Integer status;

    /*--- 表示该属性不为数据库表字段 ---*/

    @ApiModelProperty(value = "仓库名称")
    @TableField(exist = false) //表示该属性不为数据库表字段，但又是必须使用的。查询
    private String warehouseName;

    @ApiModelProperty(value = "工作台编号")
    @TableField(exist = false) //表示该属性不为数据库表字段，但又是必须使用的。查询
    private String workbenchCode;

    @ApiModelProperty(value = "工作台名称")
    @TableField(exist = false) //表示该属性不为数据库表字段，但又是必须使用的。查询
    private String workbenchName;

}
