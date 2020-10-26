package com.jayud.tms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderTransportVO {

    @ApiModelProperty(value = "子订单ID")
    private Long id;

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "货物流向(1进口 2出口)")
    private Integer goodsType;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "作业类型")
    private String classCode;

    @ApiModelProperty(value = "作业类型描述")
    private String classCodeDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    //货物信息
    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;

    @ApiModelProperty(value = "板数")
    private String plateAmount;

    @ApiModelProperty(value = "件数")
    private String pieceAmount;

    @ApiModelProperty(value = "重量")
    private String weight;

    //提供信息
    @ApiModelProperty(value = "省")//提货
    private String stateName1;

    @ApiModelProperty(value = "市")
    private String cityName1;

    @ApiModelProperty(value = "详细地址")
    private String address1;

    @ApiModelProperty(value = "省")//送货
    private String stateName2;

    @ApiModelProperty(value = "市")
    private String cityName2;

    @ApiModelProperty(value = "详细地址")
    private String address2;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;


}
