package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
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

    @ApiModelProperty(value = "申报价值", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币单位(currency_info currency_code)", position = 10)
    @JSONField(ordinal = 10)
    private String declaredCurrency;

    /*商品服务费用表*/
    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "单位(1公斤 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "服务名称(service_group code_name)")
    private String serviceName;

    @ApiModelProperty(value = "客户名称(customer company)")
    private String customerName;

}
