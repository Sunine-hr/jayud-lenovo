package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询客户账户信息
 */
@Data
public class QueryCusAccountForm extends BasePageForm{

    @ApiModelProperty(value = "客户ID")
    private Long companyId;

    @ApiModelProperty(value = "状态 0-off 1-on")
    private String status;

    @ApiModelProperty(value = "用户名")
    private String name;

}
