package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "货柜关联箱号信息list")
public class OceanCounterCustomerRelationVO {

    @ApiModelProperty(value = "主键id，自增")
    private Long id;

    @ApiModelProperty(value = "提单对应货柜信息id(ocean_counter id)")
    private Long oceanCounterId;

    @ApiModelProperty(value = "客户id(客户表 customer id)")
    private Long customerId;

    @ApiModelProperty(value = "订单对应箱号信息id(order_case id)")
    private Long orderCaseId;

    /*订单对应箱号信息:order_case*/
    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = "最终确认体积，单位 m³")
    private Double confirmVolume;

    /*产品订单表:order_info*/
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单路线(集货仓-目的仓)")
    private String orderLine;

    /*提单对应货柜信息:ocean_counter*/
    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    /*提单表:ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)")
    private String orderId;

}
