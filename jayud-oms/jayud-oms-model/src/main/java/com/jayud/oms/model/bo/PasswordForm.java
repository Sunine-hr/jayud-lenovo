package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PasswordForm {
    @ApiModelProperty(value = "旧密码")
    @NotNull(message = "oldPassword is required")
    private String oldPassword;

    @ApiModelProperty(value = "密码")
    @Size(max = 20, min = 8,message = "请输入8-20个字符")
    private String password;

    @ApiModelProperty(value = "确认密码")
    @Size(max = 20, min = 8,message = "请输入8-20个字符")
    private String confirmPassword;
}
