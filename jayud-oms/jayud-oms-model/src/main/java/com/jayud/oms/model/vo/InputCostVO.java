package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class InputCostVO {

    @ApiModelProperty(value = "应收费用")
    private List<InputPaymentCostVO> paymentCostList;

    @ApiModelProperty(value = "应付费用")
    private List<InputReceivableCostVO> receivableCostList;

}
