package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderForm {

    @ApiModelProperty(value = "主订单",required = true)
    private InputMainOrderForm orderForm;

    @ApiModelProperty(value = "报关单",required = true)
    private InputOrderCustomsForm orderCustomsForm;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit",required = true)
    private String cmd;

}
