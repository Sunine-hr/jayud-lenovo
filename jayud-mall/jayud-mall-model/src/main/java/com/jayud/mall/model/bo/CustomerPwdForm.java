package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "客户密码")
@Data
public class CustomerPwdForm {

    @ApiModelProperty(value = "手机号", position = 1)
    @JSONField(ordinal = 1)
    private String phone;

    @ApiModelProperty(value = "密码", position = 2)
    @JSONField(ordinal = 2)
    private String passwd;

    @ApiModelProperty(value = "确认密码(前端)", position = 3)
    @JSONField(ordinal = 3)
    private String affirmPasswd;

}
