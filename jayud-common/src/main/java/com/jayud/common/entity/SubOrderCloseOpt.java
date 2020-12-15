package com.jayud.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 主订单关闭操作
 */
@Data
public class SubOrderCloseOpt {

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

    @ApiModelProperty(value = "当前登录用户,FeignClient必传,要么就传token,否则跨系统拿不到用户")
    private String loginUser;


}