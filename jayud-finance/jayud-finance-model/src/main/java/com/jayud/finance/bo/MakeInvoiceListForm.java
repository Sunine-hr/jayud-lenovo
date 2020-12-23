package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 开票/付款核销
 */
@Data
public class MakeInvoiceListForm {

    @Valid
    @ApiModelProperty(value = "开票/付款对象",required = true)
    private List<MakeInvoiceForm> makeInvoices = new ArrayList<>();

    @ApiModelProperty(value = "操作指令，区分是应收-receivable还是应付-payment",required = true)
    @NotEmpty(message = "cmd is required")
    private String cmd;

    @ApiModelProperty(value = "账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "当前登录用户",required = true)
    private String loginUserName;




}
