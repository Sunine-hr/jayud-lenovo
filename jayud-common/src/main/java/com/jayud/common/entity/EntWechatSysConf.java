package com.jayud.common.entity;

import com.jayud.common.utils.Utilities;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "企业微信")
@Accessors(chain = true)
@ToString
public class EntWechatSysConf {
    @ApiModelProperty(value = "企业ID")
    private String corpid;
    @ApiModelProperty(value = "应用的凭证密钥")
    private String corpsecret;
    @ApiModelProperty(value = "企业应用的id")
    private String agentid;

    public static void main(String[] args) {
        System.out.println(Utilities.printFieldsInfo(EntWechatSysConf.class));
    }

}
