package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class QueryAccountForm extends BasePageForm {

    @ApiModelProperty("用户名")
    private String name;


    @ApiModelProperty("所属公司ID")
    private Long companyId;

    @ApiModelProperty("用户状态 0禁用，1启用")
    private Integer status;

    @ApiModelProperty("用户类型 1-用户 2-客户 3-供应商")
    @NotEmpty(message = "userType is required")
    @Pattern(regexp = "1|2|3",message = "userType requires '1' or '2' or '3' only")
    private String userType;
}
