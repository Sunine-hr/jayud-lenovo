package com.jayud.tms.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value="地址薄查询")
public class QueryDeliveryAddressForm {


    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址类型",required = true)
    @NotEmpty(message = "type is required")
    private String type;

    @ApiModelProperty(value = "客户CODE",required = true)
    @NotEmpty(message = "customerCode is required")
    private String customerCode;


}
