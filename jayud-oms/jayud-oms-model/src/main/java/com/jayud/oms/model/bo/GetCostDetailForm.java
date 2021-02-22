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
            "or zgys_sub_cost/bg_sub_cost/ky_sub_cost/hy_sub_cost子订单费用 " +
            "or zgys_sub_cost_audit/bg_sub_cost_audit/ky_sub_cost_audit/hy_sub_cost_audit子订单费用审核", required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

    @ApiModelProperty(value = "子订单类型")
    private String subType;

    public void setCmd(String cmd) {
        //重新组合
        if (cmd.contains("main")) {
            this.cmd = cmd;
        } else {
            this.cmd = cmd.substring(cmd.indexOf("_") + 1, cmd.length());
            this.subType=cmd.substring(0,cmd.indexOf("_"));
        }
//        this.cmd = cmd;
    }

    public static void main(String[] args) {
        String cmd = "zgys_sub_cost_audit";
        if (cmd.contains("main")) {
            System.out.println(cmd);
        } else {
            System.out.println(cmd.substring(cmd.indexOf("_") + 1, cmd.length()));
            System.out.println(cmd.substring(0, cmd.indexOf("_")));
        }
    }
}
