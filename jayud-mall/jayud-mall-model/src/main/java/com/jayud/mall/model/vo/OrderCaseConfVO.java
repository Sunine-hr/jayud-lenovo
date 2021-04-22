package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "订单箱号配载信息", description = "订单对应箱号配载信息:order_case、order_conf")
public class OrderCaseConfVO {

    /*订单对应箱号信息:order_case*/
    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "箱号", position = 2)
    @JSONField(ordinal = 2)
    private String cartonNo;

    @ApiModelProperty(value = "最终确认体积，单位 m³", position = 3)
    @JSONField(ordinal = 3)
    private Double confirmVolume;

    /**订单箱号配载信息**/
    /*运单表：ocean_waybill*/
    @ApiModelProperty(value = "运单号", position = 4)
    @JSONField(ordinal = 4)
    private String waybillNo;

    /*提单对应货柜信息：ocean_counter*/
    @ApiModelProperty(value = "柜号", position = 5)
    @JSONField(ordinal = 5)
    private String cntrNo;

    /*提单表：ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)(BOL)", position = 6)
    @JSONField(ordinal = 6)
    private String oceanBillNo;

    @ApiModelProperty(value = "开船日期", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程", position = 8)
    @JSONField(ordinal = 8)
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)", position = 9)
    @JSONField(ordinal = 9)
    private Integer unit;

    @ApiModelProperty(value = "预计到达时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expectedReach;

    /*配载单:order_conf*/
    @ApiModelProperty(value = "配载单号", position = 11)
    @JSONField(ordinal = 11)
    private String orderConfNo;


}
