package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySupplierServeForm extends BasePageForm{

    @ApiModelProperty(value = "服务名")
    private String serveName;

    @ApiModelProperty(value = "1海运 2空运 3陆运")
    private Integer transportPay;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    //费用项目


}
