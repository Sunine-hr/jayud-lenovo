package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShipmentIdVO {

    @ApiModelProperty(value = "主键id")
    private String shipment_id;

    @ApiModelProperty(value = "目的仓库代码(FBA仓库 fab_warehouse warehouse_code)")
    private String warehouseCode;

    @ApiModelProperty(value = "目的仓库id(FBA仓库 fab_warehouse id)")
    private Integer warehouseId;

    @ApiModelProperty(value = "服务*")
    private String service;

    @ApiModelProperty(value = "收件人邮编")
    private String postcode;

}
