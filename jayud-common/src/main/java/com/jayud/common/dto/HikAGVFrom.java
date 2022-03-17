package com.jayud.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HikAGVFrom {

    @ApiModelProperty(value = "货架移动任务明细号")
    private String mxCode;

    @ApiModelProperty(value = "订单状态(1待移动 2移动中 3已完成)")
    private Integer orderStatus;

}
