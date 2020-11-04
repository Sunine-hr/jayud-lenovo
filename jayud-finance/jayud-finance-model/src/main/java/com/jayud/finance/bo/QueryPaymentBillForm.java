package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QueryPaymentBillForm extends BasePageForm{

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户")
    private String customerName;

}
