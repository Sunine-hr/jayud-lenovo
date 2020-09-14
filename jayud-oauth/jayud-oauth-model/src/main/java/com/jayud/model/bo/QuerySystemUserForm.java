package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class QuerySystemUserForm extends BasePageForm{

    @ApiModelProperty("用户名(账号管理查询和人员审核-总经办查询)")
    private String name;

    @ApiModelProperty("用户姓名(组织架构的员工查询)")
    private String userName;

    @ApiModelProperty("所属公司ID(人员审核-总经办查询)")
    private Long companyId;

    @ApiModelProperty("审核状态:1-待审核 2-审核通过 0-审核拒绝(人员审核-总经办查询)")
    @Pattern(regexp = "0|1|2",message = "auditStatus requires '1' or '2' or '0' only")
    private String auditStatus;

    @ApiModelProperty(value = "操作指令(isAccount:账号管理的员工查询 isAudit:人员审核总经办的员工查询 byDepartmentId)")
    @Pattern(regexp = "isAccount|isAudit|byDepartmentId",message = "cmd requires 'isAccount' or 'isAudit' or 'byDepartmentId' only")
    private String cmd;

    @ApiModelProperty("所属部门ID(组织架构的员工查询)")
    private Long departmentId;
}
