package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderInfoCancelForm {

    @ApiModelProperty(value = "自增id (order_info id)")
    @NotNull(message = "订单id")
    private Long id;

    @ApiModelProperty(value = "验证密码")
    @NotBlank(message = "验证密码不能为空")
    private String validatePassword;
}
