package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomsOrderInfoForm extends BasePageForm{

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "操作指令,cmd = confirmOrder or exceptionOrder or auditFail or orderList or issueOrder or toCheck or declare or releaseConfirm")
    private String cmd;


}
