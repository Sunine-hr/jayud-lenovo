package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageForm {

    @ApiModelProperty("页码")
    private Integer currentPage = 1;

    @ApiModelProperty("页长")
    private Integer pageSize = 10;


}
