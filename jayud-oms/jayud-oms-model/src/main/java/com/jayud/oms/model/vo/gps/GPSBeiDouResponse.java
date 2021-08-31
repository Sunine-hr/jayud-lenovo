package com.jayud.oms.model.vo.gps;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通实体
 */
@Data
@Accessors(chain = true)
public class GPSBeiDouResponse {

    @Data
    public class realTimePos{
        @ApiModelProperty(value = "车牌号")
        private String carPlate;

        @ApiModelProperty(value = "车辆状态文字")
        private String stateCn;

        @ApiModelProperty(value = "方向")
        private String drct;

        @ApiModelProperty(value = "纬度")
        private String lat;

        @ApiModelProperty(value = "经度")
        private String lng;

        @ApiModelProperty(value = "速度 单位：千米/小时")
        private String speed;
    }
}
