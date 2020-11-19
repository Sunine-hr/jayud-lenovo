package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "运价查询form")
public class QueryOfferInfoFareForm extends BasePageForm{

    @ApiModelProperty(value = "国家代码")
    private String stateCode;

    @ApiModelProperty(value = "运输方式")
    private Integer tid;

    @ApiModelProperty(value = "柜型(1整柜 2散柜)")
    private Integer types;
}
