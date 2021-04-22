package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerLoginForm {

    @ApiModelProperty(value = "登录名（手机号）", position = 1, required = true)
    @NotEmpty(message = "登录名(手机号)不能为空")
    private String loginname;

    @ApiModelProperty(value = "密码", position = 2, required = true)
    @NotEmpty(message = "密码不能为空")
    private String passwd;

}
