package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "OrderCostDetailVO", description="OrderCostDetailVO,订单费用明细VO(自定义)")
@Data
//@Deprecated
public class OrderCostDetailVO {

    //费用明细-仅查询，订单应收费用
    @ApiModelProperty(value = "费用明细(订单应收费用)", position = 1)
    @JSONField(ordinal = 1)
    private List<OrderCopeReceivableVO> orderCopeReceivableVOS;

    //合计(带金额格式化)
    @ApiModelProperty(value = "费用明细(订单应收费用),合计(带金额格式化)", position = 2)
    @JSONField(ordinal = 1)
    private String orderCopeReceivableAmountTotal;

}