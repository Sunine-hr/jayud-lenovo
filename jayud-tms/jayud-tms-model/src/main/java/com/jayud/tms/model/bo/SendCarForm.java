package com.jayud.tms.model.bo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class SendCarForm {

    @ApiModelProperty(value = "主键,修改时需要")
    private Long id;

    @ApiModelProperty(value = "子订单ID",required = true)
    @NotNull(message = "orderId is required")
    private Long orderId;

    @ApiModelProperty(value = "主订单ID",required = true)
    @NotNull(message = "mainOrderId is required")
    private Long mainOrderId;

    @ApiModelProperty(value = "运输订单号",required = true)
    @NotEmpty(message = "transportNo is required")
    private String transportNo;

    @ApiModelProperty(value = "运输对应子订单",required = true)
    @NotEmpty(message = "orderNo is required")
    private String orderNo;

    @ApiModelProperty(value = "是否有六联单",required = true)
    @NotNull(message = "isHaveEncode is required")
    private Boolean isHaveEncode;

    @ApiModelProperty(value = "六联单号,勾了有就必填，没有就不填")
    private String encode;

    @ApiModelProperty(value = "六联单号文件地址,前台忽略")
    private String encodeUrl;

    @ApiModelProperty(value = "六联单号文件地址名称,前台忽略")
    private String encodeUrlName;

    @ApiModelProperty(value = "六联单号附件数组集合")
    private List<FileView> encodePics = new ArrayList<>();

    @ApiModelProperty(value = "车型(1吨车 2柜车)",required = true)
    @NotNull(message = "vehicleType is required")
    private Integer vehicleType;

    @ApiModelProperty(value = "车型(1-3T 2-5t 3-8T 4-10T)",required = true)
    @NotNull(message = "vehicleSize is required")
    private Integer vehicleSize;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号图片,前台忽略")
    private String cntrPic;

    @ApiModelProperty(value = "柜号图片名称,前台忽略")
    private String cntrPicName;

    @ApiModelProperty(value = "柜号上传附件地址数组集合")
    private List<FileView> cntrPics = new ArrayList<>();

    @ApiModelProperty(value = "供应商ID",required = true)
    @NotNull(message = "supplierInfoId is required")
    private Long supplierInfoId;

    @ApiModelProperty(value = "大陆车牌号",required = true)
    @NotEmpty(message = "licensePlate is required")
    private String licensePlate;

    @ApiModelProperty(value = "大陆司机名",required = true)
    @NotEmpty(message = "driverName is required")
    private String driverName;

    @ApiModelProperty(value = "HK车牌号",required = true)
    @NotEmpty(message = "hkLicensePlate is required")
    private String hkLicensePlate;

    @ApiModelProperty(value = "HK司机名",required = true)
    @NotEmpty(message = "hkDriverName is required")
    private String hkDriverName;

    @ApiModelProperty(value = "仓库ID)",required = true)
    @NotNull(message = "warehouseInfoId is required")
    private Long warehouseInfoId;

    @ApiModelProperty(value = "司机电话")
    private String driverPhone;

    @ApiModelProperty(value = "要求")
    private String remarks;

    @ApiModelProperty(value = "备注")
    private String describes;

    @ApiModelProperty(value = "状态,审核时传")
    private String status;

    @ApiModelProperty(value = "审核意见，审核时传")
    private String auditComment;

    @ApiModelProperty(value = "操作指令，区分派车sendCar/审核auditCar/驳回editCar",required = true)
    private String cmd;

}
