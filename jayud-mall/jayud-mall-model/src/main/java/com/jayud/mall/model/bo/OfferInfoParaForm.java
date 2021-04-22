package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OfferInfoParaForm {

    @ApiModelProperty(value = "自增id", required = true, position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "id is required")
    private Long id;


}
