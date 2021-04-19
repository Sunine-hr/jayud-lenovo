package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TdIdForm {

    @ApiModelProperty(value = "提单id", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "提单id不能为空")
    private Long tdId;

}
