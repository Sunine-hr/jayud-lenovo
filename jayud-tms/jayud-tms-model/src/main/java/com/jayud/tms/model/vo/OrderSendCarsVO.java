package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderSendCarsVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "运输订单号")
    private String transportNo;

    @ApiModelProperty(value = "是否有六联单号")
    private Boolean isHaveEncode;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "六联单号文件地址,暂时不要")
    private String encodeUrl;

    @ApiModelProperty(value = "六联单号文件地址名称,暂时不要")
    private String encodeUrlName;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片,暂时不要")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称,暂时不要")
    private String cntrPicName;

    @ApiModelProperty(value = "供应商ID")
    private Long supplierInfoId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "大陆车牌号")
    private String licensePlate;

    @ApiModelProperty(value = "大陆司机名")
    private String driverName;

    @ApiModelProperty(value = "HK车牌号")
    private String hkLicensePlate;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "审核信息")
    private Long auditInfoId;

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




}
