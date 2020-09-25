package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 物流轨迹跟踪表
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-22
 */
@Data
public class LogisticsTrackVO {

    @ApiModelProperty(value = "物流轨迹id")
    private Integer id;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "操作人")
    private String operatorUser;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operatorTime;

}
