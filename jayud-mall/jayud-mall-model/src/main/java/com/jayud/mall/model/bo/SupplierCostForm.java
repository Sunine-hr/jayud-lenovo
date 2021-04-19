package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SupplierCostForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 2)
    @JSONField(ordinal = 2)
    private Long supplierInfoId;

    @ApiModelProperty(value = "服务id(supplier_serve id)", position = 3)
    @JSONField(ordinal = 3)
    private Long serviceId;

    @ApiModelProperty(value = "费用id(cost_item id)", position = 4)
    @JSONField(ordinal = 4)
    private Long costItemId;

}
