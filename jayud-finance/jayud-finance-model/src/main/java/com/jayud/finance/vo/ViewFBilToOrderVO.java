package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应付账单列表预览
 */
@Data
public class ViewFBilToOrderVO {

    @ApiModelProperty(value = "创建日期")
    private String createdTimeStr;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "子订单编号")
    private String subOrderNo;

    @ApiModelProperty(value = "供应商")
    private String supplierChName;

    @ApiModelProperty(value = "起运地")
    private String startAddress;

    @ApiModelProperty(value = "目的地")
    private String endAddress;

    @ApiModelProperty(value = "车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "车型 如：3T")
    private String vehicleSize;

    @ApiModelProperty(value = "件数")
    private Integer pieceNum;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;


    public void interceptAddress() {
        String startAddress=this.startAddress;
        if (this.startAddress != null && this.startAddress.length() > 6) {
            startAddress=this.startAddress.substring(0,6);
        }
        String endAddress=this.endAddress;
        if (this.endAddress != null && this.endAddress.length() > 6) {
            endAddress=this.endAddress.substring(0,6);
        }
        this.startAddress=startAddress;
        this.endAddress=endAddress;
    }
}
