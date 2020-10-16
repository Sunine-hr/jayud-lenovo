package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 提货/收货地址
 */
@Data
public class InputOrderTakeAdrForm {

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "国家名称")
    private String countryName;

    @ApiModelProperty(value = "省/州名称")
    private String stateName;

    @ApiModelProperty(value = "城市名称")
    private String cityName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    //以上部分是加自定义地址使用
    //以下部分是选择地址使用,通过deliveryId区分

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

    @ApiModelProperty(value = "提货日期")
    private LocalDateTime takeTime;

    @ApiModelProperty(value = "货物描述")
    private String describe;

    @ApiModelProperty(value = "数量")
    private Double amount;

    @ApiModelProperty(value = "单位(1件 2其他)")
    private Integer unit;

    @ApiModelProperty(value = "重量")
    private Integer weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer types;

}
