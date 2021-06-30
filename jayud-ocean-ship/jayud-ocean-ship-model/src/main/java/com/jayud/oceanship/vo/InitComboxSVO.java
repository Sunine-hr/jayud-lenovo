package com.jayud.oceanship.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下拉选项
 */
@Data
public class InitComboxSVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "键")
    private String key;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

}
