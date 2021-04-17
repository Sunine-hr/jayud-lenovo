package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CountryVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "国家名称", position = 2)
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "国家简码", position = 3)
    @JSONField(ordinal = 3)
    private String code;

    @ApiModelProperty(value = "地理位置", position = 4)
    @JSONField(ordinal = 4)
    private String geo;

    @ApiModelProperty(value = "启用状态，1启用0停用", position = 5)
    @JSONField(ordinal = 5)
    private String status;

}
