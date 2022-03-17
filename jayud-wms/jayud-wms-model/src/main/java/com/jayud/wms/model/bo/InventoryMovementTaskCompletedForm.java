package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InventoryMovementTaskCompletedForm {

    @ApiModelProperty(value = "明细任务号")
    private String detailCode;

}
