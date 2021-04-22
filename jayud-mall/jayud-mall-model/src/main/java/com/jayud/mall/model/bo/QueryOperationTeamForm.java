package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOperationTeamForm extends BasePageForm{

    @ApiModelProperty(value = "小组名称", position = 1)
    @JSONField(ordinal = 1)
    private String groupName;

}
