package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VehicleInfoVO {

    @ApiModelProperty(value = "车辆ID")
    private Long id;

    @ApiModelProperty(value = "大陆车牌")
    private String plateNumber;

    @ApiModelProperty(value = "香港车牌")
    private String hkNumber;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名字")
    private String supplierName;


}
