package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "操作项")
@Data
public class ActionItemParaForm {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

}
