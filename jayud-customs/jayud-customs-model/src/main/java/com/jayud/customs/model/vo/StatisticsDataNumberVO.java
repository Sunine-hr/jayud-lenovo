package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报关各个菜单列表数据量统计
 */
@Data
public class StatisticsDataNumberVO {

    @ApiModelProperty(value = "报关接单")
    private String bgjd;

    @ApiModelProperty(value = "报关打单")
    private String bgdd;

    @ApiModelProperty(value = "报关复核")
    private String bgfh;

    @ApiModelProperty(value = "报关申报")
    private String bgsb;

    @ApiModelProperty(value = "报关放行")
    private String bgfx;

    @ApiModelProperty(value = "驳回列表")
    private String bhlb;

    @ApiModelProperty(value = "通关确认")
    private String tgqr;

    @ApiModelProperty(value = "报关异常")
    private String bgyc;


}
