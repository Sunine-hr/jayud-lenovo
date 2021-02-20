package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActionCombinationParaForm {

    @ApiModelProperty(value = "主键id", position = 1, required = true)
    @JSONField(ordinal = 1)
    @NotNull(message = "主键id不能为空")
    private Integer id;

}
