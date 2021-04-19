package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询订单对应箱号信息Form")
public class QueryOrderCaseForm extends  BasePageForm{

    @ApiModelProperty(value = "箱号", position = 1)
    private String cartonNo;
}
