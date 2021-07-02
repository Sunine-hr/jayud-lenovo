package com.jayud.finance.bo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.enums.SubOrderSignEnum;
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

    @ApiModelProperty(value = "操作指令 cmd=main_statement/zgys_statement/bg_statement/ky_statement对账单 " +
            "or statement_audit/zgys_statement_audit/bg_statement_audit/ky_statement_audit对账单审核 or" +
            " cw_statement财务对账单", required = true)
    private String cmd;

    @ApiModelProperty(value = "子订单类型")
    private String subType;

    @ApiModelProperty(value = "是否合并主订单")
    @JsonIgnore
    private Boolean isSumToMain;

    @ApiModelProperty(value = "当前登录用户名", required = true)
    private String loginUserName;

    @ApiModelProperty(value = "费用类型(0-应收, 1-应付)")
    private Integer costType;

    @ApiModelProperty(value = "制单人")
    private Integer makeUser;

    public void setCmd(String cmd) {
        this.cmd = cmd;
        setSubType(cmd.split("_")[0]);
    }

    public void setSubType(String subType) {
        this.subType = subType;
        if (SubOrderSignEnum.MAIN.getSignOne().equals(subType)) {
            this.isSumToMain = true;
            this.subType = null;
        }
    }
}
