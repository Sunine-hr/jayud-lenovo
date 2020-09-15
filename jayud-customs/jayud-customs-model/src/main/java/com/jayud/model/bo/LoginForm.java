package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 登录云报关请求
 *
 * @author william
 * @description
 * @Date: 2020-09-07 15:33
 */
@Data
@AllArgsConstructor
public class LoginForm {
    @ApiModelProperty(value = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String name;
    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "用户重复登录tokenID")
    private String token;
}
