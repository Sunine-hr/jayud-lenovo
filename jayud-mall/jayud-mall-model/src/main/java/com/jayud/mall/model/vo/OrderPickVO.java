package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderPickVO {

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

    /*提货地址基础数据表：delivery_address*/
    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "国家代码")
    private String countryCode;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省/州代码")
    private String stateCode;

    @ApiModelProperty(value = "省/州名称")
    private String stateName;

    @ApiModelProperty(value = "城市代码")
    private String cityCode;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "地址1")
    private String address;


}
