package com.jayud.trailer.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 空运订单驳回操作
 */
@Data
public class TrailerCargoRejected {

    @ApiModelProperty(value = "海运订单id")
    @NotNull(message = "海运订单id不能为空")
    private Long seaOrderId;

    @ApiModelProperty(value = "驳回原因")
    private String cause;

    @ApiModelProperty(value = "驳回选项(1:订单驳回,2:派车驳回)")
    private Integer rejectOptions;
}
