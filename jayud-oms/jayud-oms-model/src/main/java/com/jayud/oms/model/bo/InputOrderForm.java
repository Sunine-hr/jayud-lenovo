package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderForm {

    @ApiModelProperty(value = "主订单", required = true)
    private InputMainOrderForm orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsForm orderCustomsForm;

    @ApiModelProperty(value = "中港")
    private InputOrderTransportForm orderTransportForm;

    @ApiModelProperty(value = "空运")
    private InputAirOrderForm airOrderForm;

    @ApiModelProperty(value = "服务单")
    private InputOrderServiceForm orderServiceForm;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit", required = true)
    private String cmd;

}
