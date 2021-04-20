package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


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

    @ApiModelProperty(value = "海运单")
    private InputSeaOrderVO seaOrderForm;

    @ApiModelProperty(value = "内陆单")
    private InputOrderInlandTPVO orderInlandTransportForm;

    @ApiModelProperty(value = "拖车单")
    private List<InputTrailerOrderVO> trailerOrderForm;
}
