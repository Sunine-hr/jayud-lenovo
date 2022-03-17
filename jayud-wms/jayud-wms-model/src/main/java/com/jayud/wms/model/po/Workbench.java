package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Workbench 实体类
 *
 * @author jyd
 * @since 2021-12-17
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="工作台对象", description="工作台")
@TableName(value = "wms_workbench")
public class Workbench extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库库区")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "工作台编号")
    private String code;

    @ApiModelProperty(value = "工作台名称")
    private String name;

    @ApiModelProperty(value = "工作台类型(1:普通,2:分播,3:交接)")
    private Integer type;

    @ApiModelProperty(value = "AGV排队上限")
    private Integer agvNum;

    @ApiModelProperty(value = "排队货架类型(中文值)")
    private String queueShelfType;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Integer status;

    @ApiModelProperty(value = "所属仓库名称")
    @TableField(exist = false) //表示该属性不为数据库表字段，但又是必须使用的。查询
    private String warehouseName;

    @ApiModelProperty(value = "所属仓库库区名称")
    @TableField(exist = false) //表示该属性不为数据库表字段，但又是必须使用的。查询
    private String warehouseAreaName;

    @ApiModelProperty(value = "工作台类型(1:普通,2:分播,3:交接)")
    @TableField(exist = false)
    private String typeName;


}
