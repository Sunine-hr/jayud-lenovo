package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class QueryPaymentBillNumForm{

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "客户",required = true)
    @NotEmpty(message = "customerName is required")
    private String customerName;

}
