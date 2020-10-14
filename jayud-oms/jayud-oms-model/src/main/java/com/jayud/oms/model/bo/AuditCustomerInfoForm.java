package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 审核客户信息界面
 */
@Data
public class AuditCustomerInfoForm {

    @ApiModelProperty(value = "主键",required = true)
    @NotEmpty(message = "id is required")
    private Long id;

    @ApiModelProperty(value = "审核状态 0-拒绝 1-通过",required = true)
    @NotEmpty(message = "auditStatus is required")
    @Pattern(regexp = "0|1",message = "auditStatus requires '0' or '1' only")
    private String auditStatus;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "接单部门ID,客服审核时传")
    private String departmentId;

    @ApiModelProperty(value = "接单客服ID,客服审核时传")
    private Long kuId;

    @ApiModelProperty(value = "结算类型(1票结 2月结 3周结),财务审核时传")
    private Integer settlementType;

    @ApiModelProperty(value = "账期,财务审核时传")
    private Integer accountPeriod;
}
