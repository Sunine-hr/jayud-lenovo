package com.jayud.finance.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 应收/应付一致
 */
@Data
public class QueryPaymentBillDetailForm extends BasePageForm{

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "客户,应收时用")
    private String unitAccount;

    @ApiModelProperty(value = "供应商,应付时用")
    private String supplierChName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "开始核算期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginAccountTerm;

    @ApiModelProperty(value = "结束核算期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAccountTerm;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "付款申请/开票申请")
    private String applyStatus;

    @ApiModelProperty(value = "操作指令 cmd=main_statement/zgys_statement/bg_statement对账单 or statement_audit/zgys_statement_audit/bg_statement_audit对账单审核 or" +
            " cw_statement财务对账单",required = true)
    @Pattern(regexp = "(main_statement|zgys_statement|bg_statement|statement_audit|zgys_statement_audit|bg_statement_audit|" +
            "cw_statement)", message = "只允许填写特定值")
    private String cmd;

}
