package com.jayud.oms.model.vo.template.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInfoTemplate {

    @ApiModelProperty("中港订单信息")
    private Template<TmsOrderTemplate> tmsOrderTemplates;

    @ApiModelProperty("内陆订单信息")
    private Template<InlandTPTemplate> inlandTPTemplates;

    @ApiModelProperty("报关订单信息")
    private Template<OrderCustomsTemplate> orderCustomsTemplates;

    @ApiModelProperty("空运订单信息")
    private Template<AirOrderTemplate> airOrderTemplateTemplates;
}
