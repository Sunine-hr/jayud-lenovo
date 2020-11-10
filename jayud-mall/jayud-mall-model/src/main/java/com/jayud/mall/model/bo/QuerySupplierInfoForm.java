package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询供应商表单")
public class QuerySupplierInfoForm extends BasePageForm {

    @ApiModelProperty(value = "供应商名称(中)")
    private String supplierChName;

    @ApiModelProperty(value = "供应商名称(英)")
    private String supplierEnName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "关键字(供应商代码or供应商名称(中))")
    private String keyword;

}
