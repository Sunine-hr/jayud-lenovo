package com.jayud.oms.model.vo.template.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoTemplate {

    @ApiModelProperty("中港订单信息")
    private Template<TmsOrderTemplate> tmsOrderTemplates;

}
