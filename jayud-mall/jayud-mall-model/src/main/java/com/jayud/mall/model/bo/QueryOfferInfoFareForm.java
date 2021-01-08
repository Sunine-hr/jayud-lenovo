package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "运价查询form")
public class QueryOfferInfoFareForm extends BasePageForm{

    @ApiModelProperty(value = "国家代码", position = 1)
    @JSONField(ordinal = 1)
    private String stateCode;

    @ApiModelProperty(value = "运输方式", position = 2)
    @JSONField(ordinal = 2)
    private Integer tid;

    @ApiModelProperty(value = "柜型(1整柜 2散柜)", position = 3)
    @JSONField(ordinal = 3)
    private Integer types;
}
