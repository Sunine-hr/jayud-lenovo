package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryDeliveryAddressForm extends BasePageForm{

    @ApiModelProperty(value = "地址类型(1提货地址 2收货地址)", position = 1,required = true)
    @JSONField(ordinal = 1)
    @NotNull(message = "地址类型必填")
    private Integer types;

    @ApiModelProperty(value = "联系人", position = 2)
    @JSONField(ordinal = 2)
    private String contacts;

    @ApiModelProperty(value = "联系电话", position = 3)
    @JSONField(ordinal = 3)
    private String phone;

    @ApiModelProperty(value = "客户id", position = 4)
    @JSONField(ordinal = 4)
    private Integer customerId;

}
