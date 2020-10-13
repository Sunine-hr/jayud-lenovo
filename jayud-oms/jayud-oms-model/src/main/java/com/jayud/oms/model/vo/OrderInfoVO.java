package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class OrderInfoVO {

    @ApiModelProperty(value = "主订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "进出口类型")
    private String goodsTypeDesc;

    @ApiModelProperty(value = "通关口岸")
    private String portName;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "作业类型")
    private String classCodeDesc;

    @ApiModelProperty(value = "作业类型CODE")
    private String classCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "客户名称")
    private String customerCode;

    @ApiModelProperty(value = "货物信息")
    private String goodsInfo;

    @ApiModelProperty(value = "提货地址")
    private String takeAddress;

    @ApiModelProperty(value = "送货地址")
    private String giveAddress;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "是否有费用详情")
    private boolean isCost;

    @ApiModelProperty(value = "流程节点")
    private List<OrderStatusVO> statusList;

}
