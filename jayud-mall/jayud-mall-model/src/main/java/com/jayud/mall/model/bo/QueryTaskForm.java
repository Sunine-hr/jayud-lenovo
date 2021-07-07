package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryTaskForm extends BasePageForm {

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "类型(1提单任务 2运单任务)")
    private Integer types;

}
