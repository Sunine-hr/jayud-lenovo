package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderPickForm {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "提货单号")
    private String pickNo;

    @ApiModelProperty(value = "进仓单号")
    private String warehouseNo;

    @ApiModelProperty(value = "提货时间")
    private LocalDateTime pickTime;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "总体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "总箱数")
    private Integer totalCarton;

    @ApiModelProperty(value = "提货地址id(delivery_address id)")
    private Integer addressId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
