package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShipmentIdVO {

    @ApiModelProperty(value = "主键id")
    private String shipment_id;

    @ApiModelProperty(value = "目的仓库代码(FBA仓库)")
    private String warehouseCode;

}
