package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提货/收货地址
 */
@Data
public class InputOrderTakeAdrVO {

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

    @ApiModelProperty(value = "提货日期")
    private String takeTime;

    @ApiModelProperty(value = "客户")
    private String customerName;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private Double plateAmount;

    @ApiModelProperty(value = "件数")
    private Double pieceAmount;

    @ApiModelProperty(value = "箱数")
    private Double boxAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "备注")
    private String remarks;

}
