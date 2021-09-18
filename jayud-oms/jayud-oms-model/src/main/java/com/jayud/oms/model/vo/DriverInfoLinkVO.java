package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DriverInfoLinkVO {

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "司机名称")
    private String driverName;

    @ApiModelProperty(value = "大陆车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "HK车牌号")
    private String hkLicensePlate;

    @ApiModelProperty(value = "车辆信息")
    private List<VehicleInfoVO> vehicleInfoVOList = new ArrayList<>();
}
