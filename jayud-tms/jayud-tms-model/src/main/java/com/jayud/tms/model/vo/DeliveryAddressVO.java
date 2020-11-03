package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="地址薄列表展示")
public class DeliveryAddressVO {

    @ApiModelProperty(value = "id主键")
    private Long deliveryId;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省/州名称")
    private String stateName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "详细地址")
    private String address;

}
