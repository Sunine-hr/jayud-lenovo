package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SeedingWallLayout 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SeedingWallLayout对象", description = "播种位布局")
@TableName(value = "wms_seeding_wall_layout")
public class SeedingWallLayout /*extends SysBaseEntity*/ {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    protected Long id;

    @ApiModelProperty(value = "播种墙id")
    private Long seedingWallId;

    @ApiModelProperty(value = "播种位编号")
    private String code;

    @ApiModelProperty(value = "标签编号")
    private String tagNum;

    @ApiModelProperty(value = "分播优先级")
    private Integer priority;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Boolean status;

    @ApiModelProperty(value = "备用字段1")
    private Boolean columnOne;


}
