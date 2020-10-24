package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class GetCostDetailForm {

    @ApiModelProperty(value = "主订单号", required = true)
    @NotEmpty(message = "mainOrderNo is required")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号,获取子订单费用时传")
    private String subOrderNo;

    @ApiModelProperty(value = "操作指令:cmd=main_cost 主订单费用 or main_cost_audit主订单费用审核 " +
            "or sub_cost子订单费用 or sub_cost_audit子订单费用审核", required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

}
