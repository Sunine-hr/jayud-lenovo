package com.jayud.tms.model.vo;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderSendCarsVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    //TODO 2021/9/1新增
    @ApiModelProperty(value = "中港id")
    private Long transportId;

    @ApiModelProperty(value = "运输订单号")
    private String transportNo;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(3T)")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片,暂时不要")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称,暂时不要")
    private String cntrPicName;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "骑师姓名")
    public String jockey;

    @ApiModelProperty(value = "大陆车牌号")
    private String plateNumber;

    @ApiModelProperty(value = "大陆司机名")
    private String driverName;

    @ApiModelProperty(value = "司机ID")
    private Long driverInfoId;

    @ApiModelProperty(value = "HK车牌号")
    private String hkNumber;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "装货要求")
    private String remarks;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库联系人")
    private String warehouseContacts;

    @ApiModelProperty(value = "仓库联系电话")
    private String warehouseNumber;

    @ApiModelProperty(value = "仓库地址国家")
    private String countryName;

    @ApiModelProperty(value = "仓库地址省份")
    private String provinceName;

    @ApiModelProperty(value = "仓库地址市")
    private String cityName;

    @ApiModelProperty(value = "仓库详细地址")
    private String address;

    @ApiModelProperty(value = "是否虚拟仓")
    public Boolean isVirtual;

    @ApiModelProperty(value = "骑师id")
    public Long jockeyId;
    @ApiModelProperty(value = "车辆ID", required = true)
    private Long vehicleId;

    public void assembleVehicle(Object vehAndSupp) {
        if (vehAndSupp == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(vehAndSupp);
        this.plateNumber = jsonObject.getStr("plateNumber");
        this.hkNumber = jsonObject.getStr("hkNumber");
        this.supplierId = jsonObject.getLong("supplierId");
        this.supplierName = jsonObject.getStr("supplierName");
    }

    public void assembleDriver(Object driverObj) {
        if (driverObj == null) {
            return;
        }
        JSONObject jsonObject = new JSONObject(driverObj);
        this.driverName=jsonObject.getStr("name");
        this.driverPhone=jsonObject.getStr("phone");
    }
}
