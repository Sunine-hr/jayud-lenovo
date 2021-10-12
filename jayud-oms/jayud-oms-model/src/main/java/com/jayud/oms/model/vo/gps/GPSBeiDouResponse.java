package com.jayud.oms.model.vo.gps;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 通实体
 */
@Data
@Accessors(chain = true)
public class GPSBeiDouResponse {

    @Data
    public class realTimePos {
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

        @ApiModelProperty(value = "gps时间")
        private LocalDateTime time;

        @ApiModelProperty(value = "当前总里程碑")
        private Double mile;

        @ApiModelProperty(value = "地理描述")
        private String addr;

        @ApiModelProperty(value = "今日里程 单位:米")
        private Double preMile;

        @ApiModelProperty(value = "行停时长")
        private String runStopTime;
    }

    @Data
    public class historicalPos {

        @ApiModelProperty(value = "结果集合")
        private List<HistoryList> list;

        @ApiModelProperty(value = "整体数据")
        private CountData countData;

    }

    @Data
    public class HistoryList {
        @ApiModelProperty(value = "车牌号")
        private String carPlate;

        @ApiModelProperty(value = "4个字节的十六进制字符")
        private String carStts;

        @ApiModelProperty(value = "方向")
        private String drct;

        @ApiModelProperty(value = "纬度")
        private String lat;

        @ApiModelProperty(value = "经度")
        private String lng;

        @ApiModelProperty(value = "速度 单位：千米/小时")
        private String speed;

        @ApiModelProperty(value = "gps时间")
        private LocalDateTime time;

        @ApiModelProperty(value = "当前总里程碑")
        private Double mile;

        @ApiModelProperty(value = "地理描述")
        private String addr;
    }

    @Data
    public class CountData {
        @ApiModelProperty(value = "停车时长")
        private String stop_long;

        @ApiModelProperty(value = "行驶里程 公里")
        private Double mile;

    }
}
