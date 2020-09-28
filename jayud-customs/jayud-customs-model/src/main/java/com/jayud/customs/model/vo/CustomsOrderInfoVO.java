package com.jayud.customs.model.vo;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class CustomsOrderInfoVO {

    @ApiModelProperty(value = "子订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private Long mainOrderId;

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

    @ApiModelProperty(value = "委托号")
    private String entrustNo;

    @ApiModelProperty(value = "统一编号")
    private String unifiedNumber;

    @ApiModelProperty(value = "更新时间")
    private String updatedTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String remarks;

    @ApiModelProperty(value = "报关单号")
    private String yunCustomsNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

}
