package com.jayud.tms.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class InputOrderTransportVO {

    @ApiModelProperty(value = "中港订单ID")
    private Long id;

    @ApiModelProperty(value = "中港订单号")
    private String orderNo;

    @ApiModelProperty(value = "接单人")
    private String jiedanUser;

    @ApiModelProperty(value = "接单时间")
    private String jiedanTimeStr;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "货物流向描述")
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "香港车牌")
    private String hkLicensePlate;

    @ApiModelProperty(value = "大陆车牌")
    private String licensePlate;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)")
    private String vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "运输公司")
    private String supplierChName;

    @ApiModelProperty(value = "海关编号,预留字段")
    private String customsNo;

    @ApiModelProperty(value = "司机姓名")
    private String driverName;

    @ApiModelProperty(value = "司机大陆电话")
    private String driverPhone;

    @ApiModelProperty(value = "司机香港电话")
    private String driverHkPhone;

    @ApiModelProperty(value = "过磅数")
    private String carWeighNum;

    @ApiModelProperty(value = "总件数")
    private Integer totalAmount;

    @ApiModelProperty(value = "总重量")
    private Double totalWeight;

    @ApiModelProperty(value = "无缝单号")
    private String seamlessNo;

    @ApiModelProperty(value = "清关单号")
    private String clearCustomsNo;

    @ApiModelProperty(value = "装车要求")
    private String remarks;

    @ApiModelProperty(value = "柜号上传附件地址,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号上传附件地址名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "所有附件信息")
    private List<FileView> allPics = new ArrayList<>();

    //中转信息
    @ApiModelProperty(value = "中转公司")
    private String companyName;

    @ApiModelProperty(value = "联系人")
    private String contacts;

    @ApiModelProperty(value = "电话")
    private String contactNumber;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "提货地址")
    private List<InputOrderTakeAdrVO> orderTakeAdrForms1 = new ArrayList<>();

    @ApiModelProperty(value = "卸货地址")
    private List<InputOrderTakeAdrVO> orderTakeAdrForms2 = new ArrayList<>();




}
