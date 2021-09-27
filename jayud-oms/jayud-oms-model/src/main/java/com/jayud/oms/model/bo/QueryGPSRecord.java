package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryGPSRecord {

    @ApiModelProperty(value = "统计维度(1:车辆维度 2订单维度)")
    private Integer type;
    @ApiModelProperty(value = "供应商")
    private Long supplierId;
    @ApiModelProperty(value = "车牌")
    private String licensePlate;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "子订单类型")
    private String subType;
    @ApiModelProperty(value = "子订单号")
    private String orderNo;
}
