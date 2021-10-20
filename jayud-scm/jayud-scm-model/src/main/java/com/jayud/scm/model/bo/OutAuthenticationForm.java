package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 外部认证信息
 */
@Data
public class OutAuthenticationForm {

    @ApiModelProperty(value = "账号",required = true)
    private String loginname;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

    @ApiModelProperty(value = "加密后的值，loginname+时间轴+password",required = true)
    private String sign;

    @ApiModelProperty(value = "数据",required = true)
    private BizData bizData;

}