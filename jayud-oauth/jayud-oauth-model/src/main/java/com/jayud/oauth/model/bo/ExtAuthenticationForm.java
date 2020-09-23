package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 外部认证信息
 */
@Data
public class ExtAuthenticationForm {

    @ApiModelProperty(value = "appId",required = true)
    private String appId;

    @ApiModelProperty(value = "加密后的值，appId+时间轴+appSecret",required = true)
    private String sign;

    @ApiModelProperty(value = "数据",required = true)
    private BizData bizData;


}
