package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderConfVerifyForm {

    @ApiModelProperty(value = "自增id")
    @NotNull(message = "配载单id不能为空")
    private Long id;

    @ApiModelProperty(value = "验证密码", notes = "取消时，输入jdy，确认后才能取消")
    @NotBlank(message = "验证密码不能为空")
    private String verifyPassword;


}
