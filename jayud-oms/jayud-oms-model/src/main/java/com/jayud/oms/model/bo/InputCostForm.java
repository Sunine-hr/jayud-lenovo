package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class InputCostForm {

    @ApiModelProperty(value = "主订单ID", required = true)
    private Long mainOrderId;

    @ApiModelProperty(value = "子订单号", required = true)
    private String orderNo;

    @ApiModelProperty(value = "子订单法人主体")
    private String subLegalName;

    @ApiModelProperty(value = "子订单客户")
    private String subCustomerName;

    @ApiModelProperty(value = "应付费用", required = true)
    private List<InputPaymentCostForm> paymentCostList;

    @ApiModelProperty(value = "应收费用", required = true)
    private List<InputReceivableCostForm> receivableCostList;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit_main or submit_main or preSubmit_sub or submit_sub", required = true)
    private String cmd;

}
