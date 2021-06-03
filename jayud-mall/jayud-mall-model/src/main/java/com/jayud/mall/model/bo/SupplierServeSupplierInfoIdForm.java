package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierServeSupplierInfoIdForm {

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Long supplierInfoId;

}
