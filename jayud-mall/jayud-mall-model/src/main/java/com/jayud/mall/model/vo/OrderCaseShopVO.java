package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCaseShopVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "箱号id(order_case id)")
    private Long caseId;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private String cartonNo;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Integer orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
