package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class QueryCustomsFinanceCoRelationForm extends BasePageForm {

    @ApiModelProperty(value = "云报关公司名称")
    private String yunbaoguanName;

    @ApiModelProperty(value = "金蝶公司名称")
    private String kingdeeName;

    @ApiModelProperty(value = "金蝶公司代码（客户对应应收，为CUS开头。供应商对应应付，VEN开头）")
    private String kingdeeCode;

    @ApiModelProperty(value = "是否废弃（1-是，0-否）")
    private Integer deprecated;
}
