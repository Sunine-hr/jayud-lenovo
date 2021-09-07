package com.jayud.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "钉钉系统配置")
@Accessors(chain = true)
@ToString
public class DingDingSysConf {
    @ApiModelProperty(value = "发送消息时使用的微应用的AgentID")
    private String agentid;
    @ApiModelProperty(value = "应用的唯一标识key")
    private String appkey;
    @ApiModelProperty(value = "应用的密钥")
    private String appsecret;

}
