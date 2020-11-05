package com.jayud.tools.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SensitiveCommodityVO {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "品名")
    private String name;

}
