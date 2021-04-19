package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "客户验证")
@Data
public class CustomerVerifyForm {

    @ApiModelProperty(value = "手机号", position = 1)
    @JSONField(ordinal = 1)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码(前端)", position = 2)
    @JSONField(ordinal = 2)
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

}
