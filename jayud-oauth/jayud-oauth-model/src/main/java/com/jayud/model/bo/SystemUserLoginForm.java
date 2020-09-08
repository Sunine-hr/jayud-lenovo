package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 系统用户登录参数
 * @author chuanmei
 */
@Data
public class SystemUserLoginForm {

    @ApiModelProperty(value = "用户名",name="username", required = true)
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;
    @ApiModelProperty("是否保持登录")
    private Boolean keepLogin;
}
