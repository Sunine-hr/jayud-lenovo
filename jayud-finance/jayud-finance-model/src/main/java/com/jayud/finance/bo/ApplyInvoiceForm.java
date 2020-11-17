package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
public class ApplyInvoiceForm {

    @ApiModelProperty(value = "账单编号",required = true)
    @NotNull(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "开票金额",required = true)
    @NotNull(message = "invoiceAmount is required")
    private BigDecimal invoiceAmount;

}
