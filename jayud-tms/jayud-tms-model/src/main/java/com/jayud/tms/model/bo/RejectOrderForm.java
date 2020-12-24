package com.jayud.tms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RejectOrderForm {

    @ApiModelProperty(value = "子订单ID",required = true)
    @NotNull(message = "orderId is required")
    private Long orderId;

    @ApiModelProperty(value = "操作指令 cmd=T_5_1 车辆提货驳回",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

    @ApiModelProperty(value = "驳回选项(1:驳回订单,2:驳回派车)")
    private Integer rejectOptions;

    @ApiModelProperty(value = "驳回原因")
    private String cause;
}
