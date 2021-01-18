package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(value = "OrderCostDetailVO", description="OrderCostDetailVO,订单费用明细VO(自定义)")
@Data
public class OrderCostDetailVO {

    //费用明细
    @ApiModelProperty(value = "费用明细", position = 1)
    @JSONField(ordinal = 1)
    private List<TemplateCopeReceivableVO> costDetails;

    //合计(带金额格式化)
    @ApiModelProperty(value = "合计(带金额格式化)", position = 1)
    @JSONField(ordinal = 1)
    private String amountTotal;

}