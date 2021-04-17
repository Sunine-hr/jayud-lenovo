package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryShipmentForm extends BasePageForm {

    @ApiModelProperty(value = "运单id")
    private String shipment_id;

}
