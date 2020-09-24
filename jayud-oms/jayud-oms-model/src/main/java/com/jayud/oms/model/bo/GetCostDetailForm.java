package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class GetCostDetailForm {

    @ApiModelProperty(value = "主订单号", required = true)
    private Long mainOrderNo;

    @ApiModelProperty(value = "操作指令:cmd=main_cost 主订单费用 or sub_cost子订单费用", required = true)
    private String cmd;

}
