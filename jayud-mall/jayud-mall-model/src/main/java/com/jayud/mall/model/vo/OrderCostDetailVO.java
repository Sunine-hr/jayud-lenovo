package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "OrderCostDetailVO", description="OrderCostDetailVO,订单费用明细VO(自定义)")
@Data
public class OrderCostDetailVO {

    //费用明细-仅查询，订单应收费用
    @ApiModelProperty(value = "费用明细(订单应收费用)", position = 1)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOS;

    //订单应收费用合计(分币种)
    @ApiModelProperty(value = "订单应收费用合计(分币种)", notes = "费用明细(订单应收费用),多币种")
    private List<AggregateAmountVO> orderCopeReceivableAggregate;


}