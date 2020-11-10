package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class EditBillForm {

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "被删除的费用集合",required = true)
    @NotNull(message = "delCostIds is required")
    private List<Long> delCostIds;

    @ApiModelProperty(value = "新增的费用集合",required = true)
    @NotEmpty(message = "paymentBillDetailForms is required")
    private List<OrderPaymentBillDetailForm> paymentBillDetailForms;


}
