package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InitComboxVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "值")
    private String name;

    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;

}
