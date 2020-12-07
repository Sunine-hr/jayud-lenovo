package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下拉选项
 */
@Data
public class InitComboxVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "值")
    private String name;

}
