package com.jayud.Inlandtransport.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提货/收货地址
 */

@Data
public class TakeGoodsInfoVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private String vehicleSize;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private String vehicleTypeDesc;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "提货日期")
    private String takeTimeStr;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;


}
