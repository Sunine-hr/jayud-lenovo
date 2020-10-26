package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 重置密码表单
 */
@Data
public class ResetUserPwdForm {

    @ApiModelProperty(value = "用户ID 修改时必传")
    private Long id;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    private String password;

}
