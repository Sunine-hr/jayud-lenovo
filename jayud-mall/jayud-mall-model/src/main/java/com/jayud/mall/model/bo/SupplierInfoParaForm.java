package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SupplierInfoParaForm {

    @ApiModelProperty(value = "自增ID", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "id不能为空")
    private Long id;

}
