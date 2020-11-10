package com.jayud.oms.model.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class OprCostBillForm {

    @ApiModelProperty(value = "应付出账单界面部分")
    private List<Long> costIds;

    @ApiModelProperty(value = "操作应付还是应收")
    private String oprType;

    @ApiModelProperty(value = "操作指令 cmd=pre_create暂存 or create生成账单 or del删除对账单费用")
    private String cmd;

}
