package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 下拉选项
 */
@Data
public class InitComboxSVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "值")
    private String value;

}
