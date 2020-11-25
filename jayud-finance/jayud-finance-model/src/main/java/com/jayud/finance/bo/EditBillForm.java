package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
public class EditBillForm {

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "对账单主表ID",required = true)
    @NotNull(message = "billId is required")
    private Long billId;

    @ApiModelProperty(value = "被删除的费用集合",required = true)
    @NotNull(message = "delCosts is required")
    private List<OrderPaymentBillDetailForm> delCosts;

    @ApiModelProperty(value = "新增的费用集合",required = true)
    @NotEmpty(message = "paymentBillDetailForms is required")
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;

    @ApiModelProperty(value = "操作指令 cmd=save保存 submit提交",required = true)
    @Pattern(regexp = "(save|submit)", message = "只允许填写save or submit")
    private String cmd;

    @ApiModelProperty(value = "当前登录用户",required = true)
    private String loginUserName;

}
