package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QueryCurrencyRateForm extends BasePageForm {

    @ApiModelProperty(value = "本币(currency_info id)")
    private Integer dcid;

    @ApiModelProperty(value = "他币(currency_info id)")
    private Integer ocid;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

}
