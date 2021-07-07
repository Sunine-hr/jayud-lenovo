package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderConfForm extends BasePageForm {

    @ApiModelProperty(value = "配载单号")
    private String orderNo;

    @ApiModelProperty(value = "目的国家代码(country code)")
    private String destinationCountryCode;

    @ApiModelProperty(value = "状态代码" +
            "" +
            "0:准备" +
            "10:启用" +
            "20:开始配载" +
            "30:转运中" +
            "40:完成" +
            "-1:取消")
    private String statusCode;

}
