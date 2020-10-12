package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderVO {

    @ApiModelProperty(value = "主订单")
    private InputMainOrderVO orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsVO orderCustomsForm;

}
