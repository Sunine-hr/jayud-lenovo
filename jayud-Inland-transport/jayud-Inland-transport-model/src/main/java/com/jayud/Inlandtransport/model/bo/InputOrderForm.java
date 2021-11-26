package com.jayud.Inlandtransport.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderForm {

    @ApiModelProperty(value = "主订单", required = true)
    private InputMainOrderIfForm orderForm;

    @ApiModelProperty(value = "内陆")
    private InputOrderInlandTransportForm  orderInlandTransportForm;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;

    @ApiModelProperty(value = "登录人")
    private String loginUserName;
}
