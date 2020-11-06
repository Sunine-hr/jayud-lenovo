package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierInfoForm {

    @ApiModelProperty(value = "关键字(供应商代码or供应商名称(中))")
    private String keyword;

}
