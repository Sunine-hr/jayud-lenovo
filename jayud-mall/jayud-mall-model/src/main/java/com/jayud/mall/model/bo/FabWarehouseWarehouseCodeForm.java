package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FabWarehouseWarehouseCodeForm {

    @ApiModelProperty(value = "仓库代码")
    private String warehouseCode;

}
