package com.jayud.oauth.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 增加法人主体
 */
@Data
public class AddLegalEntityForm {

    @ApiModelProperty(value = "法人主体ID",required = true)
    @NotEmpty(message = "法人主体ID不能为空")
    private Long id;

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "法人主体不能为空")
    private String legalName;

    @ApiModelProperty(value = "主体编号",required = true)
    @NotEmpty(message = "主体编号不能为空")
    private String legalCode;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "销售部门")
    private Long saleDepartId;
}
