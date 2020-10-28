package com.jayud.tools.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础分页表单
 */
@Data
public class BasePageForm {

    @ApiModelProperty("页码")
    private Integer pageNum = 1;

    @ApiModelProperty("页长")
    private Integer pageSize = 10;


}
