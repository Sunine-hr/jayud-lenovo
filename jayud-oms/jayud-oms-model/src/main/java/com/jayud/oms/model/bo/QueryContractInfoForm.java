package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 查询合同信息界面
 */
@Data
public class QueryContractInfoForm extends BasePageForm{

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同类型 0:客户合同 1:供应商合同",required = true)
    @NotEmpty(message = "type is required")
    private String type;

    @ApiModelProperty(value = "当前登录用户")
    private String loginUserName;

}
