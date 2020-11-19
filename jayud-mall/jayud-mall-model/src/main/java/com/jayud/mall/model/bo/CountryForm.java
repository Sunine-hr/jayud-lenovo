package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountryForm {

    @ApiModelProperty(value = "国家名称")
    private String name;

    @ApiModelProperty(value = "国家简码")
    private String code;

    @ApiModelProperty(value = "地理位置")
    private String geo;

    @ApiModelProperty(value = "启用状态，1是0否")
    private String status;

}
