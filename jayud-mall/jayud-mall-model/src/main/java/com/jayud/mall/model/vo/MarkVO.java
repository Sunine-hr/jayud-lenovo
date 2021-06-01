package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MarkVO {

    //箱唛-即订单箱号
    @ApiModelProperty(value = "箱唛")
    private String cartonNo;

    //进仓编号
    @ApiModelProperty(value = "进仓编号")
    private String warehouseNo;

    //箱数比例(第几箱，第几份) 1/10  2/10 3/10 ... 10/10
    @ApiModelProperty(value = "箱数比例(第几箱，第几份)")
    private String cartonRatio;

}
