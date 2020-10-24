package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InitChangeStatusVO {

    @ApiModelProperty(value = "子订单ID")
    private Long id;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;

}
