package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 *暂存/提交对账单
 */
@Data
public class CreatePaymentBillForm {

    @ApiModelProperty(value = "应付出账单界面部分",required = true)
    @NotEmpty(message = "paymentBillForm is required")
    private OrderPaymentBillForm paymentBillForm;

    @ApiModelProperty(value = "应付出账单详情界面部分",required = true)
    @NotEmpty(message = "paymentBillDetailForms is required")
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;

    @ApiModelProperty(value = "账单类别",required = true)
    @Pattern(regexp = "(main|zgys|bg)", message = "只允许填写main or zgys or bg")
    private String subType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单",required = true)
    @Pattern(regexp = "(pre_create|create)", message = "只允许填写pre_create or create")
    private String cmd;

}
