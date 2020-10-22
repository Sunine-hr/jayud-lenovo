package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TakeGoodsInfoVO {

    @ApiModelProperty(value = "法人主体中文名")
    private String legalName;

    @ApiModelProperty(value = "法人主体英文名")
    private String enLegalName;

    @ApiModelProperty(value = "电话")
    private String tel;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "接单时间")
    private String jiedanTimeStr;

    @ApiModelProperty(value = "提货信息")
    private List<SendCarPdfVO> takeInfo1 = new ArrayList();

    @ApiModelProperty(value = "送货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "联系人")
    private String deliveryContacts;

    @ApiModelProperty(value = "联系电话")
    private String deliveryPhone;

    @ApiModelProperty(value = "装车要求")
    private String remarks;

    @ApiModelProperty(value = "大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private Integer vehicleSize;

    @ApiModelProperty(value = "车型(1吨车 2柜车)")
    private Integer vehicleType;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "进出口")
    private String goodsType;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单人联系电话")
    private String jiedanPhone;

    @ApiModelProperty(value = "香港清关地址")
    private String clearCustomsAddress;

    @ApiModelProperty(value = "货物信息")
    private List<GoodsInfoVO> goddsInfos = new ArrayList<>();

    @ApiModelProperty(value = "总件数")
    private Integer totalPieceAmount;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "总体积")
    private Double totalVolume;


}
