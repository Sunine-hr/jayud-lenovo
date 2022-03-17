package com.jayud.wms.model.vo;

import com.jayud.wms.model.po.SeedingWallLayout;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 播种墙布局
 */
@Data
public class SeedingWallLayoutVo {

    @ApiModelProperty(value = "播种墙id")
    private Long seedingWallId;

    /*

    @ApiModelProperty(value = "行")      三行
    @TableField("`row`")
    private Integer row;

    @ApiModelProperty(value = "列")      四列
    @TableField("`column`")
    private Integer column;

    */


//    @ApiModelProperty(value = "播种墙行布局")
//    private List<SeedingWallLayoutRowVo> rows;

    @ApiModelProperty(value = "播种墙行布局[row][column]")
    SeedingWallLayout[][] layout;


}
