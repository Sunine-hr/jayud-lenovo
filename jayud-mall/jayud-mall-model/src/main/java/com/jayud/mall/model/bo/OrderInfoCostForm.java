package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoCostForm {

    /*订单对应应收费用明细:order_cope_receivable*/
    @ApiModelProperty(value = "订单对应应收费用明细:order_cope_receivable", position = 1)
    @JSONField(ordinal = 1)
    private List<OrderCopeReceivableForm> orderCopeReceivableVOList;

    /*订单对应应付费用明细:order_cope_with*/
    @ApiModelProperty(value = "订单对应应付费用明细:order_cope_with", position = 1)
    @JSONField(ordinal = 1)
    private List<OrderCopeWithForm> orderCopeWithVOList;

}
