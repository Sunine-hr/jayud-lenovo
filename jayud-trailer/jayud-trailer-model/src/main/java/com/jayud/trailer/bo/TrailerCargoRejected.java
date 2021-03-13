package com.jayud.trailer.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 拖车订单驳回操作
 */
@Data
public class TrailerCargoRejected {

    @ApiModelProperty(value = "拖车订单id")
    @NotNull(message = "拖车订单id不能为空")
    private Long trailerOrderId;

    @ApiModelProperty(value = "驳回原因")
    private String cause;

    @ApiModelProperty(value = "驳回选项(1:订单驳回,2:派车驳回)")
    private Integer rejectOptions;
}
