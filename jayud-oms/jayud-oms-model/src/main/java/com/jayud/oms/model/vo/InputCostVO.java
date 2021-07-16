package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
public class InputCostVO {

    @ApiModelProperty(value = "应收费用")
    private List<InputPaymentCostVO> paymentCostList = new ArrayList<>();

    @ApiModelProperty(value = "应付费用")
    private List<InputReceivableCostVO> receivableCostList = new ArrayList<>();

    @ApiModelProperty(value = "利润")
    private BigDecimal profit;

    @ApiModelProperty(value = "应收币种")
    private String currencyReceivable;

    @ApiModelProperty(value = "应付币种")
    private String currencyPayable;
}
