package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class OprCostBillForm {

    @ApiModelProperty(value = "应付出账单界面部分")
    private List<Long> costIds;

    @ApiModelProperty(value = "操作应付还是应收")
    private String oprType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create主订单出账暂存 or create主订单生成账单" +
            "    or        pre_create_zgys中港运输出账暂存 or create_zgys中港运输生成账单" +
            "    or        pre_create_bg报关出账暂存 or create_bg报关生成账单 or del删除对账单")
    private String cmd;

}
