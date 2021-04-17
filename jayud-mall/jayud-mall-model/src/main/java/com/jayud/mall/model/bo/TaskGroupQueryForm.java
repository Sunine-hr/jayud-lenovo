package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskGroupQueryForm {

    @ApiModelProperty(value = "类型(1提单任务分组 2运单任务分组)", position = 1)
    @JSONField(ordinal = 1)
    private Integer types;

    @ApiModelProperty(value = "分组代码", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "分组名称", position = 3)
    @JSONField(ordinal = 3)
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

}
