package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PhoneForm {

    @ApiModelProperty(value = "中国大陆手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;
}
