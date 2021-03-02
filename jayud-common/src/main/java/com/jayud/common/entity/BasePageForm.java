package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePageForm {

    @ApiModelProperty("页码")
    private Integer pageNum = 1;

    @ApiModelProperty("页长")
    private Integer pageSize = 10;


}
