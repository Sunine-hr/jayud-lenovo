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

    @ApiModelProperty(value = "操作指令,cmd = confirmOrder接单 or exceptionOrder异常单 or auditFail审核不通过 or orderList订单列表 or " +
            "issueOrder打单 or toCheck复核 or declare申报 or releaseConfirm放行确认")
    private String cmd;


}
