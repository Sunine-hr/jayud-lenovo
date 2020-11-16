package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderShopVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单编号(order_info id)")
    private Integer orderId;

    @ApiModelProperty(value = "商品编号(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "数量")
    private Integer quantity;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /*客户商品表:customer_goods*/
    @ApiModelProperty(value = "商品编码")
    private String sku;

    @ApiModelProperty(value = "海关编码")
    private String hsCode;

    @ApiModelProperty(value = "中文名")
    private String nameCn;

}
