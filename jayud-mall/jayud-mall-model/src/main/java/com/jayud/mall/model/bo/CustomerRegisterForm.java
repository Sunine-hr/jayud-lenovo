package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "客户注册")
@Data
public class CustomerRegisterForm {

    @ApiModelProperty(value = "用户名，即登录名", position = 1)
    @JSONField(ordinal = 1)
    private String userName;

    @ApiModelProperty(value = "密码", position = 2)
    @JSONField(ordinal = 2)
    private String passwd;

    @ApiModelProperty(value = "确认密码(前端)", position = 3)
    @JSONField(ordinal = 3)
    private String affirmPasswd;

    @ApiModelProperty(value = "手机号", position = 4)
    @JSONField(ordinal = 4)
    private String phone;

    @ApiModelProperty(value = "验证码(前端)", position = 5)
    @JSONField(ordinal = 5)
    private String verificationCode;

    @ApiModelProperty(value = "公司名称", position = 6)
    @JSONField(ordinal = 6)
    @NotBlank(message = "公司名称不能为空")
    private String company;

}
