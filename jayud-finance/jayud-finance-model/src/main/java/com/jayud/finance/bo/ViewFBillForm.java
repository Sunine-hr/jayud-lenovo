package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 出应付对账单之前的暂存预览
 */
@Data
public class ViewFBillForm {

    @ApiModelProperty(value = "应付出账单界面部分")
    private List<OrderPaymentBillDetailForm> billDetailForms = new ArrayList<>();

    @ApiModelProperty(value = "操作指令，cmd = main or bg or zgys",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

    @ApiModelProperty(value = "模板操作指令", required = true)
    private String templateCmd;

    @ApiModelProperty(value = "展示维度(1:费用项展示,2:订单维度)", required = true)
    @NotNull(message = "type is required")
    private Integer type=1;


}
