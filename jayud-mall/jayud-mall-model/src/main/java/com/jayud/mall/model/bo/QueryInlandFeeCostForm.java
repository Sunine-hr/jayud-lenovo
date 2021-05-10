package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryInlandFeeCostForm extends BasePageForm {


    @ApiModelProperty(value = "内陆费代码(cost_item cost_code)")
    private String costCode;

    @ApiModelProperty(value = "内陆费名称(cost_item cost_name)")
    private String costName;

    @ApiModelProperty(value = "起运地-国家")
    private String fromCountry;

    @ApiModelProperty(value = "起运地-省州")
    private String fromProvince;

    @ApiModelProperty(value = "起运地-城市")
    private String fromCity;

    @ApiModelProperty(value = "起运地-区县")
    private String fromRegion;

    @ApiModelProperty(value = "目的地-国家")
    private String toCountry;

    @ApiModelProperty(value = "目的地-省州")
    private String toProvince;

    @ApiModelProperty(value = "目的地-城市")
    private String toCity;

    @ApiModelProperty(value = "目的地-区县")
    private String toRegion;

}
