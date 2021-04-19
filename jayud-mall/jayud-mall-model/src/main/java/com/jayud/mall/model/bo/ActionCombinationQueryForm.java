package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "查询操作项组合")
@Data
public class ActionCombinationQueryForm {

    @ApiModelProperty(value = "操作项组合名称", position = 2)
    @JSONField(ordinal = 2)
    private String combinationName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 3)
    @JSONField(ordinal = 3)
    private String status;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 5)
    @JSONField(ordinal = 5)
    private String userName;

}
