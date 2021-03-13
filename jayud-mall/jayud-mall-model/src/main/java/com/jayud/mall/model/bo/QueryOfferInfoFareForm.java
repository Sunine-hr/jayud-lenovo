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

    @ApiModelProperty(value = "柜型(1整柜 2拼箱)", position = 3)
    @JSONField(ordinal = 3)
    private Integer types;

    @ApiModelProperty(value = "客户id(当前登录客户id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer customerId;

    @ApiModelProperty(value = "目的仓库id(fab_warehouse id)", position = 5)
    @JSONField(ordinal = 5)
    private Integer arriveWarehouseId;
}
