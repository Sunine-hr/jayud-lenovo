package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ServiceTypeVO {
    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "type服务类型")
    private String type;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;
}
