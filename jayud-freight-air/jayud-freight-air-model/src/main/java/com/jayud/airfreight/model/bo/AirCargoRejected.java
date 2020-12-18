package com.jayud.airfreight.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 空运订单驳回操作
 */
@Data
public class AirCargoRejected {

    @ApiModelProperty(value = "空运订单id")
    @NotNull(message = "空运订单id不能为空")
    private Long airOrderId;

    @ApiModelProperty(value = "驳回原因")
    private String cause;

    @ApiModelProperty(value = "驳回选项(1:订单驳回,2:订舱驳回)")
    private Integer rejectOptions;
}
