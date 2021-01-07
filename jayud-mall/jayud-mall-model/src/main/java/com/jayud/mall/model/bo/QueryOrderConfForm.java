package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderConfForm extends BasePageForm {

    @ApiModelProperty(value = "配载单号", position = 1)
    @JSONField(ordinal = 1)
    private String orderNo;

    @ApiModelProperty(value = "目的国家代码(country code)", position = 2)
    @JSONField(ordinal = 2)
    private String destinationCountryCode;


}
