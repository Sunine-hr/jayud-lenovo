package com.jayud.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "邮件")
@Accessors(chain = true)
@ToString
public class MapEntity {

    @ApiModelProperty(value = "地址名称")
    private String addrName;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "维度")
    private Double latitude;
}
