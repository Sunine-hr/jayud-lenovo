package com.jayud.scm.model.bo;

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

    @ApiModelProperty(value = "英文名")
    private String legalEnName;

    @ApiModelProperty(value = "电话")
    @NotEmpty(message = "电话不能为空")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "开户银行")
    @NotEmpty(message = "开户银行不能为空")
    private String bank;

    @ApiModelProperty(value = "开户账户")
    @NotEmpty(message = "开户账户不能为空")
    private String accountOpen;

    @ApiModelProperty(value = "纳税识别号")
    @NotEmpty(message = "纳税识别号不能为空")
    private String taxIdentificationNum;
}
