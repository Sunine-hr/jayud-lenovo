package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "OrderBillVO", description = "订单账单")
@Data
public class OrderBillVO {

    @ApiModelProperty(value = "订单id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "配载信息 配载单号+提单号+柜号", position = 3)
    @JSONField(ordinal = 3)
    private String confInfo;

    @ApiModelProperty(value = "订单应收费用", position = 4)
    @JSONField(ordinal = 4)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOS;

    @ApiModelProperty(value = "订单应付费用", position = 5)
    @JSONField(ordinal = 5)
    private List<OrderCopeWithVO> orderCopeWithVOS;

}