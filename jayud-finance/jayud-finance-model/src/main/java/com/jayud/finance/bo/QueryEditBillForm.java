package com.jayud.finance.bo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 应收/应付一致
 */
@Data
public class QueryEditBillForm extends BasePageForm{

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "操作指令,cmd=main or bg or zgys",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;


}
