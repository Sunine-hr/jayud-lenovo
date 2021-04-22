package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryTaskGroupForm extends BasePageForm {

    @ApiModelProperty(value = "分组名称(前端：任务组名称)", position = 4)
    @JSONField(ordinal = 4)
    private String groupName;

}
