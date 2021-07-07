package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CustomerUpdatePwdForm {

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    private String passwd;

    @ApiModelProperty(value = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPasswd;

    @ApiModelProperty(value = "确认新密码")
    @NotBlank(message = "确认新密码不能为空")
    private String affirmPasswd;

}
