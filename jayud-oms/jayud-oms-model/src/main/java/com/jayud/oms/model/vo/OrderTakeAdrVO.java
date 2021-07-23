package com.jayud.oms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 提货/收货地址
 */
@Data
public class OrderTakeAdrVO {

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "提货日期")
    private String takeTimeStr;

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

    @ApiModelProperty(value = "经度")
    private Double latitude;

    @ApiModelProperty(value = "纬度")
    private Double longitude;


}
