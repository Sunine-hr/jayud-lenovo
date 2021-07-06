package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInteriorStatusVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "主状态类型(front前端 after后端)")
    private String mainStatusType;

    @ApiModelProperty(value = "主状态代码")
    private String mainStatusCode;

    @ApiModelProperty(value = "主状态名称")
    private String mainStatusName;

    @ApiModelProperty(value = "内部状态唯一码")
    private String interiorStatusCode;

    @ApiModelProperty(value = "内部状态名称")
    private String interiorStatusName;

    @ApiModelProperty(value = "状态标志")
    private String statusFlag;
}
