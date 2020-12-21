package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提货/收货地址
 */
@Data
public class InputOrderTakeAdrVO {

    @ApiModelProperty(value = "地址信息(delivery_address id)")
    private Long deliveryId;

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

    @ApiModelProperty(value = "提货/送货信息ID")
    private Long takeAdrId;

    @ApiModelProperty(value = "提货日期")
    private String takeTimeStr;

    @ApiModelProperty(value = "客户,取主订单的客户")
    private String customerName;

    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private Integer plateAmount;

    @ApiModelProperty(value = "件数")
    private Integer pieceAmount;

    @ApiModelProperty(value = "箱数,预留字段")
    private Integer boxAmount;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "类型(1提货 2收货)")
    private Integer oprType;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private String vehicleTypeDesc;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private String vehicleSizeDesc;

    @ApiModelProperty(value = "入仓号,送货地址特有")
    private String enterWarehouseNo;

    public String getVehicleTypeDesc() {
        if(this.vehicleType != null){
            if(this.vehicleType == 1){
                return "吨车";
            }else if(this.vehicleType == 2){
                return "柜车";
            }
        }
        return "";
    }

    public String getVehicleSizeDesc() {
        if(this.vehicleSize != null){
            if(this.vehicleSize == 1){
                return "3T";
            }else if(this.vehicleSize == 2){
                return "5T";
            }else if(this.vehicleSize == 3){
                return "8T";
            }else if(this.vehicleSize == 4){
                return "10T";
            }
        }
        return "";
    }

}
