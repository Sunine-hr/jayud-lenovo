package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询合同信息界面
 */
@Data
public class QueryContractInfoForm extends BasePageForm{

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "客户名称")
    private String name;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

}
