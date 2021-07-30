package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加客户账号
 */
@Data
public class AddCusAccountForm extends BasePageForm{

    @ApiModelProperty(value = "主键，修改时传")
    private Long id;

    @ApiModelProperty(value = "姓名",required = true)
    @NotEmpty(message = "userName is required")
    private String userName;

    @ApiModelProperty(value = "登录名",required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "英文名")
    private String enUserName;

    @ApiModelProperty(value = "角色ID",required = true)
    @NotEmpty(message = "roleId is required")
    private Long roleId;

    @ApiModelProperty(value = "所属公司ID",required = true)
    @NotEmpty(message = "companyId is required")
    private Long companyId;

    @ApiModelProperty(value = "法人主体ID集合",required = true)
    @NotNull(message = "legalEntityIds is required")
    private List<Long> legalEntityIds = new ArrayList<>();

    @ApiModelProperty(value = "所属上级ID",required = true)
    @NotEmpty(message = "departmentChargeId is required")
    private Long departmentChargeId;

    private String userType;



}
