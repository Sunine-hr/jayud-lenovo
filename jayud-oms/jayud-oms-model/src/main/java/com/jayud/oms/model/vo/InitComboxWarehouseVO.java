package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InitComboxWarehouseVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "仓库名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;
}
