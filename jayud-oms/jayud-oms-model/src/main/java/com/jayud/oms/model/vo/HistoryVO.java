package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GPS实时定位车辆返回对象
 */
@Data
public class HistoryVO {

    @ApiModelProperty(value = "车辆点火方向")
    private Integer accState;

    @ApiModelProperty(value = "行驶方向")
    private Integer direction;

    @ApiModelProperty(value = "经度")
    private Double latitude;

    @ApiModelProperty(value = "纬度")
    private Double longitude;

    @ApiModelProperty(value = "终端上报位置的时间")
    private String reportTime;

    @ApiModelProperty(value = "速度")
    private Double speed;

    @ApiModelProperty(value = "终端记录的总里程")
    private Double starkMileage;

}
