package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DriverInfoLinkVO {

    @ApiModelProperty(value = "供应商ID")
    private Long supplierInfoId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "大陆车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "HK车牌号")
    private String hkLicensePlate;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;


}
