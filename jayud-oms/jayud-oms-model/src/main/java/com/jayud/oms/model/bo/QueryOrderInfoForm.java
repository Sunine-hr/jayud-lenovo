package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInfoForm extends BasePageForm{

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸")
    private String portCode;

    @ApiModelProperty(value = "作业类型")
    private String bizCode;

    @ApiModelProperty(value = "操作指令,cmd = noSubmit or submit costAudit")
    private String cmd;


}
