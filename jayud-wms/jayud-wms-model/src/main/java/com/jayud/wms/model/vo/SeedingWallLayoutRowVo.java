package com.jayud.wms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 播种墙布局 --> 行
 */
@Data
public class SeedingWallLayoutRowVo {

    @ApiModelProperty(value = "播种墙`列`布局")
    private List<SeedingWallLayoutColumnVo> columns;
}
