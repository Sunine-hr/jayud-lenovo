package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OfferInfoIdForm {

    @ApiModelProperty(value = "运价id(报价id)", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "运价id(报价id)不能为空")
    private Integer offerInfoId;

}
