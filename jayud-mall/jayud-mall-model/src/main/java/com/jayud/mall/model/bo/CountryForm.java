package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountryForm {

    @ApiModelProperty(value = "国家名称", position = 1)
    @JSONField(ordinal = 1)
    private String name;

    @ApiModelProperty(value = "国家简码", position = 2)
    @JSONField(ordinal = 2)
    private String code;

    @ApiModelProperty(value = "地理位置", position = 3)
    @JSONField(ordinal = 3)
    private String geo;

    @ApiModelProperty(value = "启用状态，1是0否", position = 4)
    @JSONField(ordinal = 4)
    private String status;

}
