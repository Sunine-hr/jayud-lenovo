package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "客户手机")
@Data
public class CustomerPhoneForm {

    @ApiModelProperty(value = "主键id，自增", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "id不能为空")
    private Integer id;

    @ApiModelProperty(value = "手机号", position = 2)
    @JSONField(ordinal = 2)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "验证码(前端)", position = 3)
    @JSONField(ordinal = 3)
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

}
