package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeliveryAddressForm {

    @ApiModelProperty(value = "自增id")
    private Integer id;

    @ApiModelProperty(value = "客户ID(customer id)")
    private Integer customerId;

    @ApiModelProperty(value = "类型(1提货地址 2收货地址)")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省/州代码")
    private String stateCode;

    @ApiModelProperty(value = "省/州名称")
    private String stateName;

    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地址1")
    private String address;

    @ApiModelProperty(value = "地址2")
    private String address2;

    @ApiModelProperty(value = "地址3")
    private String address3;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "仓库代码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    private Integer status;

}
