package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CostItemSupForm {

    @ApiModelProperty(value = "供应商信息id", position = 1)
    @JSONField(ordinal = 1)
    @NotEmpty(message = "供应商信息id必填")
    private String supplierInfoId;

}
