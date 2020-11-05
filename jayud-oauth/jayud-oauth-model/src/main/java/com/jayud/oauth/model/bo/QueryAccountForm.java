package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryAccountForm extends BasePageForm {

    @ApiModelProperty("用户名")
    private String name;


    @ApiModelProperty("所属公司ID")
    private Long companyId;


    @ApiModelProperty("用户状态 0禁用，1启用")
    private Integer status;

    @ApiModelProperty("用户类型 1-用户 2-客户 3-供应商")
    private String userType;
}
