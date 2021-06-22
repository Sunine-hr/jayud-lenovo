package com.jayud.finance.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 应收/应付一致
 */
@Data
public class QueryVoidBillingForm extends BasePageForm {

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户,应收时用")
    private String unitAccount;

    @ApiModelProperty(value = "供应商,应付时用")
    private String supplierChName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "核算期")
    private String accountTermStr;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "付款申请/开票申请")
    private String applyStatus;

//    @ApiModelProperty(value = "")
//    private String cmd;

    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "当前登录用户名", required = true)
    private String loginUserName;

//    public void setCmd(String cmd) {
//        this.cmd = cmd;
//        this.subType = cmd.split("_")[0];
//    }
}
