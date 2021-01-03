package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OceanCounterWaybillRelationVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "货柜id(ocean_counter id)", position = 2)
    @JSONField(ordinal = 2)
    private Long oceanCounterId;

    @ApiModelProperty(value = "运单id(订单id)(order_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Long orderInfoId;


}
