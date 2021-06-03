package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierCostServerIdForm {

    @ApiModelProperty(value = "服务id(supplier_serve id)")
    private Long serviceId;

}
