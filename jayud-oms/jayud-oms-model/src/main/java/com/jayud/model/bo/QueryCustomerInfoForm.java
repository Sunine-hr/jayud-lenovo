package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询客户信息界面
 */
@Data
public class QueryCustomerInfoForm extends BasePageForm{

    @ApiModelProperty(value = "客户ID，查看客户详情时传")
    private Long id;

    @ApiModelProperty(value = "客户名")
    private String name;

}
