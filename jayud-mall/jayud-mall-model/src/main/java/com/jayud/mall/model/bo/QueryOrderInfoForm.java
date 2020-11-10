package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInfoForm extends BasePageForm {

    @ApiModelProperty(value = "订单号")
    private String orderNo;

}
