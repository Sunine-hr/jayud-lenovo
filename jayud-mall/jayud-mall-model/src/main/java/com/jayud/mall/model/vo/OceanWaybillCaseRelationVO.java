package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="OceanWaybillCaseRelationVO对象", description="运单对应箱号关联表")
public class OceanWaybillCaseRelationVO {

    @ApiModelProperty(value = "主键Id，自增")
    private Long id;

    @ApiModelProperty(value = "运单id(ocean_waybill id)")
    private Long oceanWaybillId;

    @ApiModelProperty(value = "订单对应箱号信息id(order_case id)")
    private Long orderCaseId;

    @ApiModelProperty(value = "客户id(customer id)")
    private Long customerId;

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

    /*运单表：ocean_waybill*/
    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    /*提单对应货柜信息:ocean_counter*/
    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    /*提单表:ocean_bill*/
    @ApiModelProperty(value = "提单号(供应商提供)")
    private String orderId;


}
