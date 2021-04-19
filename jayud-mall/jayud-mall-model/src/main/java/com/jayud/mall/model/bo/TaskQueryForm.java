package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskQueryForm {

    @ApiModelProperty(value = "任务代码", position = 1)
    @JSONField(ordinal = 1)
    private String taskCode;

    @ApiModelProperty(value = "任务名称", position = 2)
    @JSONField(ordinal = 2)
    private String taskName;

}