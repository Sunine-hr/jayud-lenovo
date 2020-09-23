package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderVO {

    @ApiModelProperty(value = "主订单",required = true)
    private InputMainOrderVO orderForm;

    @ApiModelProperty(value = "报关单",required = true)
    private InputOrderCustomsVO orderCustomsForm;

}
