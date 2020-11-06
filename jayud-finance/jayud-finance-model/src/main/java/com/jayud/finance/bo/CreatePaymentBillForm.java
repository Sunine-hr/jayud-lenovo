package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class CreatePaymentBillForm {

    @ApiModelProperty(value = "应付出账单界面部分",required = true)
    @NotEmpty(message = "paymentBillForm is required")
    private OrderPaymentBillForm paymentBillForm;

    @ApiModelProperty(value = "应付出账单详情界面部分",required = true)
    @NotEmpty(message = "paymentBillDetailForms is required")
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单" +
            " or pre_create_zgys中港运输出账暂存 or create_zgys中港运输生成账单" +
            " or pre_create_bg报关出账暂存 or create_bg报关生成账单",required = true)
    @Pattern(regexp = "(pre_create|create|pre_create_zgys|create_zgys|pre_create_bg|create_bg)", message = "只允许填写pre_create or create or pre_create_zgys or create_zgys" +
            "or pre_create_bg or create_bg")
    private String cmd;

}
