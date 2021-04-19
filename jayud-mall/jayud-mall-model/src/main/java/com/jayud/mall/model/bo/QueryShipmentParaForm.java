package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryShipmentParaForm {

    @ApiModelProperty(value = "运单id")
    @NotNull(message = "运单id不能为空")
    private String shipment_id;

}
