package com.jayud.oms.model.vo.gps;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 云港通实体
 */
@Data
@Accessors(chain = true)
public class GPSYGTResponse {

    @ApiModelProperty(value = "车牌号")
    private String LicenceNumber;

    @ApiModelProperty(value = "车辆点火状态，1 点火，0 熄火")
    private Integer AccState;

    @ApiModelProperty(value = "方向")
    private Integer Direction;

    @ApiModelProperty(value = "纬度")
    private Double Latitude;

    @ApiModelProperty(value = "经度")
    private Double Longitude;

    @ApiModelProperty(value = "速度")
    private Double Speed;

    @ApiModelProperty(value = "终端上报位置的时间")
    private LocalDateTime ReportTime;

    @ApiModelProperty(value = "终端记录的总里程,km，精确到小数点后 2 位")
    private Double StarkMileage;
}
