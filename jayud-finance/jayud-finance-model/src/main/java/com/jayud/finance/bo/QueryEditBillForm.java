package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QueryEditBillForm extends BasePageForm{

    @ApiModelProperty(value = "对账单编号")
    private String billNo;


}
