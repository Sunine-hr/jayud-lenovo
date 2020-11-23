package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderReceiveBillDetailForm {

    @ApiModelProperty(value = "账单详情ID,有则必传")
    private Long billDetailId;

    @ApiModelProperty(value = "订单编号",required = true)
    @NotEmpty(message = "orderNo is required")
    private String orderNo;

    @ApiModelProperty(value = "业务类型",required = true)
    @NotEmpty(message = "bizCodeDesc is required")
    private String bizCodeDesc;

    @ApiModelProperty(value = "创建日期",required = true)
    @NotEmpty(message = "createdTimeStr is required")
    private String createdTimeStr;

    @ApiModelProperty(value = "客户",required = true)
    @NotEmpty(message = "customerName is required")
    private String customerName;

    @ApiModelProperty(value = "起运地,纯报关没有")
    private String startAddress;

    @ApiModelProperty(value = "目的地,纯报关没有")
    private String endAddress;

    @ApiModelProperty(value = "车牌号,纯报关没有")
    private String licensePlate;

    @ApiModelProperty(value = "报关单号,仅报关有")
    private String yunCustomsNo;

    @ApiModelProperty(value = "费用类型",required = true)
    @NotEmpty(message = "costGenreName is required")
    private String costGenreName;

    @ApiModelProperty(value = "费用类别",required = true)
    @NotEmpty(message = "costTypeName is required")
    private String costTypeName;

    @ApiModelProperty(value = "费用名称",required = true)
    @NotEmpty(message = "costName is required")
    private String costName;

    @ApiModelProperty(value = "人民币")
    private BigDecimal rmb;

    @ApiModelProperty(value = "美元")
    private BigDecimal dollar;

    @ApiModelProperty(value = "欧元")
    private BigDecimal euro;

    @ApiModelProperty(value = "港币")
    private BigDecimal hKDollar;

    @ApiModelProperty(value = "费用类型/类别/名称维度的本币金额",required = true)
    @NotNull(message = "localAmount is required")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "费用备注")
    private String remarks;

    @ApiModelProperty(value = "应付费用ID",required = true)
    @NotNull(message = "costId is required")
    private Long costId;

    @ApiModelProperty(value = "车型 如：3T",required = true)
    @NotEmpty(message = "vehicleSize is required")
    private String vehicleSize;

    @ApiModelProperty(value = "订单维度的件数",required = true)
    @NotNull(message = "pieceNum is required")
    private Integer pieceNum;

    @ApiModelProperty(value = "订单维度的重量",required = true)
    @NotNull(message = "weight is required")
    private Double weight;

}
