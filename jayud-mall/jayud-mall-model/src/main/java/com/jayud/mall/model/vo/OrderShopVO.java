package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderShopVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "订单编号(order_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer orderId;

    @ApiModelProperty(value = "商品编号(customer_goods id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer goodId;

    @ApiModelProperty(value = "数量", position = 4)
    @JSONField(ordinal = 4)
    private Integer quantity;

    @ApiModelProperty(value = "创建时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*客户商品表:customer_goods*/
    @ApiModelProperty(value = "商品编码", position = 6)
    @JSONField(ordinal = 6)
    private String sku;

    @ApiModelProperty(value = "海关编码", position = 7)
    @JSONField(ordinal = 7)
    private String hsCode;

    @ApiModelProperty(value = "中文名", position = 8)
    @JSONField(ordinal = 8)
    private String nameCn;

}
