package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 播种墙布局 --> 列
 */
@Data
public class SeedingWallLayoutColumnVo /*extends SysBaseEntity*/ {

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


}
