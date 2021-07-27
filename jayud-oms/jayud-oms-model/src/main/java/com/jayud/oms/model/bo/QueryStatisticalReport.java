package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询统计报表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryStatisticalReport {

    @ApiModelProperty(value = "时间区间")
    private List<String> timeInterval;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty(value = "时间类型(1:年,2:月)")
    private Integer timeType;

    @ApiModelProperty(value = "sql时间格式")
    @JsonIgnore
    private String sqlFormat;

    @ApiModelProperty(value = "时间补充年份/月份")
    @JsonIgnore
    private List<String> suppleTimeData = new ArrayList<>();

    @ApiModelProperty(value = "时间补充年份/月份简写")
    @JsonIgnore
    private List<String> suppleTimeDataShort = new ArrayList<>();

    @ApiModelProperty(value = "时间单位")
    @JsonIgnore
    private String timeUnit;

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(QueryStatisticalReport.class));
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
        switch (timeType) {
            case 1:
                sqlFormat = "%Y";
                timeUnit="年";
                break;
            case 2:
                sqlFormat = "%Y-%m";
                timeUnit="月";
                break;
        }
    }

    public void assemblyTime() {
        if (!StringUtils.isEmpty(this.startTime) && !StringUtils.isEmpty(this.endTime)) {
            timeInterval = new ArrayList<>();
            timeInterval.add(startTime);
            timeInterval.add(endTime);
        }

    }

    public void supplementaryTimeData() {
        if (timeType == null || timeType == 2) {
            String startYear = null;
            String endYear = null;
            boolean isDefault = false;
            if (CollectionUtils.isEmpty(timeInterval)) {
                startYear = DateUtils.LocalDateTime2Str(LocalDateTime.now(), "YYYY");
                this.setTimeType(2);
                isDefault = true;
            } else {
                startYear = timeInterval.get(0).split("-")[0];
                endYear = timeInterval.get(1).split("-")[0];
            }

            for (int i = 1; i <= 12; i++) {
                String month = i >= 10 ? i + "" : "0" + i;
                suppleTimeData.add(startYear + "-" + month);
                suppleTimeDataShort.add(startYear + "-" + month);
            }
            if (endYear != null) {
                for (int i = 1; i <= 12; i++) {
                    String month = i >= 10 ? i + "" : "0" + i;
                    suppleTimeData.add(endYear + "-" + month);
                    suppleTimeDataShort.add(endYear + "-" + month);
                }
            }
            //默认本年月份
            if (isDefault) {
                timeInterval=new ArrayList<>();
                timeInterval.add(suppleTimeData.get(0));
                timeInterval.add(suppleTimeData.get(suppleTimeData.size() - 1));
            }
        } else {
            if (CollectionUtils.isNotEmpty(timeInterval)) {
                int interval = Integer.parseInt(timeInterval.get(0)) - Integer.parseInt(timeInterval.get(1));
                for (int i = 1; i <= interval; i++) {
                    suppleTimeData.add(Integer.parseInt(timeInterval.get(0)) + i + "");
                    suppleTimeDataShort.add(Integer.parseInt(timeInterval.get(0)) + i + "");
                }
            }
        }
    }
}
