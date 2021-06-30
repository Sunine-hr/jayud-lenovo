package com.jayud.oceanship.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 下拉选项
 */
@Data
public class InitComboxListVO {

    @ApiModelProperty(value = "主键")
    private List<InitComboxSVO> initComboxSVOList;

    @ApiModelProperty(value = "默认值")
    private String defaultValue;

}
