package com.jayud.common.entity;

import com.jayud.common.utils.FileView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel(description = "邮件")
@Accessors(chain = true)
@ToString
public class EmailSysConf {
    @ApiModelProperty(value = "发送服务地址")
    private String url;
    @ApiModelProperty(value = "发送邮箱账号")
    private String account;
    @ApiModelProperty(value = "发送邮箱账号名称")
    private String name;
    @ApiModelProperty(value = "邮箱密码")
    private String pwd;

}
