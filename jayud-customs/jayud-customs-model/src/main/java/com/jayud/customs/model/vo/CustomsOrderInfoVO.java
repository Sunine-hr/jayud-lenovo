package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class CustomsOrderInfoVO {

    @ApiModelProperty(value = "子订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单")
    private String orderNo;

    @ApiModelProperty(value = "进出口类型")
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关文件")
    private List<FileView> fileViews;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "附件")
    private String fileStr;

}
