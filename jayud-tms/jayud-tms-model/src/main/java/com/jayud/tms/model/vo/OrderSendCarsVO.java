package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderSendCarsVO {

    @ApiModelProperty(value = "派车ID")
    private Long id;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "装货要求")
    private String remarks;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "仓库联系人")
    private String warehouseContacts;

    @ApiModelProperty(value = "仓库联系电话")
    private String warehouseNumber;

    @ApiModelProperty(value = "仓库地址国家")
    private String countryName;

    @ApiModelProperty(value = "仓库地址省份")
    private String provinceName;

    @ApiModelProperty(value = "仓库地址市")
    private String cityName;

    @ApiModelProperty(value = "仓库详细地址")
    private String address;




}
