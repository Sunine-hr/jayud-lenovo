package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class ApplyPaymentForm {

    @ApiModelProperty(value = "账单编号",required = true)
    @NotNull(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "申请付款金额",required = true)
    @NotNull(message = "paymentAmount is required")
    private BigDecimal paymentAmount;

}
