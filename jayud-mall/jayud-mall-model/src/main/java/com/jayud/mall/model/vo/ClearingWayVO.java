package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClearingWayVO {

    @ApiModelProperty(value = "主键id(clearing_way id)")
    private Integer id;

    @ApiModelProperty(value = "结算方式名称")
    private String name;


}
