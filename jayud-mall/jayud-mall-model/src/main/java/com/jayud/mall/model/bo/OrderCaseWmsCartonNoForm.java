package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderCaseWmsCartonNoForm {

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

}
