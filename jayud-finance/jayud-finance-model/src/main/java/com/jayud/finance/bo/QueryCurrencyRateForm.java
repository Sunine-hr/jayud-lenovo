package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 汇率管理分页查询
 */
@Data
public class QueryCurrencyRateForm extends BasePageForm{

    @ApiModelProperty(value = "月份")
    private String month;


}
