package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCurrencyInfoForm extends BasePageForm {

    @ApiModelProperty(value = "币种代码", position = 1)
    @JSONField(ordinal = 1)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 2)
    @JSONField(ordinal = 2)
    private String currencyName;

    @ApiModelProperty(value = "国家代码", position = 3)
    @JSONField(ordinal = 3)
    private String countryCode;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

}
