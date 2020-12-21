package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatisticsDataNumberVO {

    @ApiModelProperty(value = "运输接单")
    private String ysjd;

    @ApiModelProperty(value = "运输派车")
    private String yspc;

    @ApiModelProperty(value = "运输审核")
    private String yssh;

    @ApiModelProperty(value = "驳回重新调用")
    private String bhcxdy;

    @ApiModelProperty(value = "确认派车")
    private String qrpc;

    @ApiModelProperty(value = "车辆提货")
    private String clth;

    @ApiModelProperty(value = "车辆过磅")
    private String clgb;

    @ApiModelProperty(value = "通关前复核")
    private String tgqfh;

    @ApiModelProperty(value = "车辆通关")
    private String cltg;

    @ApiModelProperty(value = "香港清关")
    private String xgqg;

    @ApiModelProperty(value = "车辆入仓")
    private String clrc;

    @ApiModelProperty(value = "车辆出仓")
    private String clcc;

    @ApiModelProperty(value = "车辆派送")
    private String clps;

    @ApiModelProperty(value = "确认签收")
    private String qrqs;

}
