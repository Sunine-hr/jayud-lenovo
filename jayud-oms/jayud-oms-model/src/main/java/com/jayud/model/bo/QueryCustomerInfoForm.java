package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询客户信息界面
 */
@Data
public class QueryCustomerInfoForm extends BasePageForm{

    @ApiModelProperty(value = "客户名")
    private String name;

}
