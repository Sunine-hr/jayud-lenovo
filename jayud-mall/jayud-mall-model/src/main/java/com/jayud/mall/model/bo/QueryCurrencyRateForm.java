package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QueryCurrencyRateForm extends BasePageForm {

    @ApiModelProperty(value = "本币(currency_info id)", position = 1)
    @JSONField(ordinal = 1)
    private Integer dcid;

    @ApiModelProperty(value = "他币(currency_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer ocid;

    @ApiModelProperty(value = "汇率", position = 3)
    @JSONField(ordinal = 3)
    private BigDecimal exchangeRate;

}
