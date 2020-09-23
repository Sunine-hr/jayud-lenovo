package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 外部认证信息
 */
@Data
public class BizData {

    @ApiModelProperty(value = "时间轴YYYYMMDDHHMMSS",required = true)
    private String oprTime;



}
