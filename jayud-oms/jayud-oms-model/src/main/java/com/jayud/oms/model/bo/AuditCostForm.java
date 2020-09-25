package com.jayud.oms.model.bo;

import com.jayud.oms.model.po.OrderPaymentCost;
import com.jayud.oms.model.po.OrderReceivableCost;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class AuditCostForm {

    @ApiModelProperty(value = "应付", required = true)
    private List<OrderPaymentCost> paymentCosts;

    @ApiModelProperty(value = "应收", required = true)
    private List<OrderReceivableCost> receivableCosts;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "审核状态 3-通过 0-驳回", required = true)
    private String status;

}
