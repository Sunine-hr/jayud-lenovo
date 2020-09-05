package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddLegalEntityForm {

    @ApiModelProperty(value = "法人主体ID",required = true)
    private Long id;

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "主体编号")
    private String legalCode;

    @ApiModelProperty(value = "注册所在地")
    private String rigisAddress;

    @ApiModelProperty(value = "销售部门")
    private Long saleDepartId;
}
