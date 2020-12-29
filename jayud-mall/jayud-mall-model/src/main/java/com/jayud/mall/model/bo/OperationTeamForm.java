package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationTeamForm {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "小组代码", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "小组名称", position = 3)
    @JSONField(ordinal = 3)
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

}
