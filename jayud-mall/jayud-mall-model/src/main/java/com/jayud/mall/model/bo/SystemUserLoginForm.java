package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author mfc
 * @description:
 * @date 2020/10/23 15:50
 */
@Data
public class SystemUserLoginForm {

    @ApiModelProperty(value = "登录名（手机号／邮箱）", required = true)
    @NotEmpty(message = "登录名（手机号／邮箱）不能为空")
    private String loginname;
    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

}
