package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账单预览费用分类
 */
@Data
public class ViewBillToCostClassVO {

    @ApiModelProperty(value = "字段名")
    private String name;

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "子订单号")
    private String subOrderNo;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

}
