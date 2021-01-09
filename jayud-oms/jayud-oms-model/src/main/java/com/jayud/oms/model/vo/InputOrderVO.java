package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputOrderVO {

    @ApiModelProperty(value = "主订单")
    private InputMainOrderVO orderForm;

    @ApiModelProperty(value = "报关单")
    private InputOrderCustomsVO orderCustomsForm;

    @ApiModelProperty(value = "中港单")
    private InputOrderTransportVO orderTransportForm;

    @ApiModelProperty(value = "空运单")
    private InputAirOrderVO airOrderForm;

    @ApiModelProperty(value = "服务单")
    private InputOrderServiceVO orderServiceForm;
}
