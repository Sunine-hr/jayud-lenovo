package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderInfoForm extends BasePageForm {

    @ApiModelProperty(value = "订单号", position = 1)
    @JSONField(ordinal = 1)
    private String orderNo;

}
