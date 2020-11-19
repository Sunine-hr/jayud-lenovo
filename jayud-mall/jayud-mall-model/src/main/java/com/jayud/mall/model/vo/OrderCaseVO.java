package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "订单对应箱号信息VO")
public class OrderCaseVO {

    @ApiModelProperty(value = "自增id", position = 1)
    private Long id;

    @ApiModelProperty(value = "订单id(order_info.id)", position = 2)
    private Integer orderId;

    @ApiModelProperty(value = "箱号", position = 3)
    private String cartonNo;

    @ApiModelProperty(value = "FAB箱号", position = 4)
    private String fabNo;

    @ApiModelProperty(value = "客户测量的长度，单位cm", position = 5)
    private BigDecimal asnLength;

    @ApiModelProperty(value = "客户测量的宽，单位cm", position = 6)
    private BigDecimal asnWidth;

    @ApiModelProperty(value = "客户测量的高度，单位cm", position = 7)
    private BigDecimal asnHeight;

    @ApiModelProperty(value = "客户测量的重量，单位kg", position = 8)
    private BigDecimal asnWeight;

    @ApiModelProperty(value = "预报长宽高计算得到的体积，单位m³", position = 9)
    private BigDecimal asnVolume;

    @ApiModelProperty(value = "客户填写时间", position = 10)
    private LocalDateTime asnWeighDate;

    @ApiModelProperty(value = "仓库测量的长度，单位cm", position = 11)
    private BigDecimal wmsLength;

    @ApiModelProperty(value = "仓库测量的高度，单位cm", position = 12)
    private BigDecimal wmsHeight;

    @ApiModelProperty(value = "仓库测量的宽度，单位cm", position = 13)
    private BigDecimal wmsWidth;

    @ApiModelProperty(value = "仓库测量的重量，单位kg", position = 14)
    private BigDecimal wmsWeight;

    @ApiModelProperty(value = "仓库计量长宽高得到的体积，单位m³", position = 15)
    private BigDecimal wmsVolume;

    @ApiModelProperty(value = "仓库测量时间", position = 16)
    private LocalDateTime wmsWeighDate;

    @ApiModelProperty(value = "最终确认重量，单位kg", position = 17)
    private BigDecimal confirmLength;

    @ApiModelProperty(value = "最终确认高度，单位cm", position = 18)
    private BigDecimal confirmHeight;

    @ApiModelProperty(value = "最终确认宽度，单位cm", position = 19)
    private BigDecimal confirmWidth;

    @ApiModelProperty(value = "最终确认重量，单位cm", position = 20)
    private BigDecimal confirmWeight;

    @ApiModelProperty(value = "最终确认体积，单位 m³", position = 21)
    private BigDecimal confirmVolume;

    @ApiModelProperty(value = "最终确定时间", position = 22)
    private LocalDateTime confirmWeighDate;

    @ApiModelProperty(value = "是否已确认（0,1）", position = 23)
    private Integer status;

    @ApiModelProperty(value = "备注", position = 24)
    private String remark;

    /*客户预报长宽高*/
    @ApiModelProperty(value = "客户预报长宽高")
    private String asnLwg;

    /*订单信息*/
    @ApiModelProperty(value = "订单号", position = 25)
    private String orderNo;

    @ApiModelProperty(value = "客户ID，对应customer.id", position = 26)
    private Integer customerId;

    /*客户信息*/
    @ApiModelProperty(value = "中文名，联系人", position = 27)
    private String nameCn;

    @ApiModelProperty(value = "公司名", position = 28)
    private String company;

    /**订单箱号配载信息**/
    /*运单表：ocean_waybill*/
    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    /*提单对应货柜信息：ocean_counter*/
    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    /*提单表：ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)")
    private String oceanBillNo;

    /*配载单:order_conf*/
    @ApiModelProperty(value = "配载单号")
    private String orderConfNo;


}
