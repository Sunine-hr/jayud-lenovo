package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseNameVO {

    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "货架名称")
    private String shelvesName;

}
