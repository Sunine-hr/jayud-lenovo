package com.jayud.model.bo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 上传委托单附件form
 *
 * @author william
 * @description
 * @Date: 2020-09-08 09:18
 */
@Data
public class PushAppendixForm {
    @ApiModelProperty(value = "委托单头UID")
    @NotNull(message = "uid 不能为空")
    private String uid;
    @ApiModelProperty(value = "文件名，必须包含文件后缀名")
    @NotEmpty(message = "文件名不能为空")
    private String fname;
    @ApiModelProperty(value = "文件类型，对应枚举AppendixTypeEnum")
    @NotEmpty(message = "文件类型不能为空")
    private String ftype;
    @ApiModelProperty(value = "文件Base64加密后的字符串")
    private String data;
}
