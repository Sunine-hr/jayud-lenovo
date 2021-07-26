package com.jayud.oms.model.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(QueryStatisticalReport.class));
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
        switch (timeType) {
            case 1:
                sqlFormat = "%Y";
                break;
            case 2:
                sqlFormat = "%Y-%m";
                break;
        }
    }

    private void assemblyTime() {
        if (!StringUtils.isEmpty(this.startTime) && !StringUtils.isEmpty(this.endTime)) {
            timeInterval.add(startTime);
            timeInterval.add(endTime);
        }

    }
}
