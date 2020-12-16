package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DriverInfoLinkVO {

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "车辆信息")
    private List<VehicleInfoVO> vehicleInfoVOList = new ArrayList<>();


}
