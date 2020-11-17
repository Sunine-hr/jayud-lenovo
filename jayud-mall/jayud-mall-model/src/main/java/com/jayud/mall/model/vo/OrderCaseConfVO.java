package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "订单箱号配载信息", description = "订单对应箱号配载信息:order_case、order_conf")
public class OrderCaseConfVO {

    /*订单对应箱号信息:order_case*/
    @ApiModelProperty(value = "自增id", position = 1)
    private Long id;

    @ApiModelProperty(value = "箱号", position = 3)
    private String cartonNo;

    @ApiModelProperty(value = "最终确认体积，单位 m³", position = 21)
    private Double confirmVolume;

    /**订单箱号配载信息**/
    /*运单表：ocean_waybill*/
    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    /*提单对应货柜信息：ocean_counter*/
    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    /*提单表：ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)(BOL)")
    private String oceanBillNo;

    @ApiModelProperty(value = "开船日期")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "航程")
    private Integer voyageDay;

    @ApiModelProperty(value = "单位(1小时 2天 3月)")
    private Integer unit;

    @ApiModelProperty(value = "预计到达时间")
    private LocalDateTime expectedReach;

    /*配载单:order_conf*/
    @ApiModelProperty(value = "配载单号")
    private String orderConfNo;


}
