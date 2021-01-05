package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryDeliveryAddressForm extends BasePageForm{

    @ApiModelProperty(value = "类型(1提货地址 2收货地址)", position = 1)
    @JSONField(ordinal = 1)
    private Integer types;

    @ApiModelProperty(value = "联系人", position = 2)
    @JSONField(ordinal = 2)
    private String contacts;

    @ApiModelProperty(value = "联系电话", position = 3)
    @JSONField(ordinal = 3)
    private String phone;

}
