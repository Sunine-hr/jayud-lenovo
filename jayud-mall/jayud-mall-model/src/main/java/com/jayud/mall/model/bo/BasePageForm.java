package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础分页表单
 */
@Data
public class BasePageForm {

    @ApiModelProperty(value = "页码", position = 101)
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页长", position = 102)
    private Integer pageSize = 10;

}
