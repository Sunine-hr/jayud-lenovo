package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 审核客户信息界面
 */
@Data
public class QueryRelUnitInfoListForm {

    @ApiModelProperty(value = "客户ID",required = true)
    @NotEmpty(message = "customerInfoId is required")
    private Long customerInfoId;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

}
