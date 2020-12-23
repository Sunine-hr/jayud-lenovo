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

    @ApiModelProperty(value = "子订单结算单位CODE")
    private String subUnitCode;

    @ApiModelProperty(value = "应付费用", required = true)
    private List<InputPaymentCostForm> paymentCostList;

    @ApiModelProperty(value = "应收费用", required = true)
    private List<InputReceivableCostForm> receivableCostList;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit_main or submit_main or preSubmit_sub or submit_sub", required = true)
    private String cmd;

    @ApiModelProperty(value = "区分费用类型，主订单=main 中港订单=zgys 报关=bg", required = true)
    private String subType;

}
