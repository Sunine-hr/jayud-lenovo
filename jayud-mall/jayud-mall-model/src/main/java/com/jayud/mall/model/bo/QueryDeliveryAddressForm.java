package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryDeliveryAddressForm extends BasePageForm{

    @ApiModelProperty(value = "类型(1提货地址 2收货地址)")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

}
