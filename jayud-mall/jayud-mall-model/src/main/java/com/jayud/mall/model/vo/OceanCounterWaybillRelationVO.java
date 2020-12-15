package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OceanCounterWaybillRelationVO {

    @ApiModelProperty(value = "主键id", position = 1)
    private Long id;

    @ApiModelProperty(value = "货柜id(ocean_counter id)", position = 2)
    private Long oceanCounterId;

    @ApiModelProperty(value = "运单id(订单id)(order_info id)", position = 3)
    private Long orderInfoId;


}
