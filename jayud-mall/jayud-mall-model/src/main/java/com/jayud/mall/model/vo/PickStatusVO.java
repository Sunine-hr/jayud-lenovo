package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "PickStatusVO", description = "提货状态")
@Data
public class PickStatusVO {

    @ApiModelProperty(value = "提货单号", position = 1)
    @JSONField(ordinal = 1)
    private String pickNo;

    @ApiModelProperty(value = "进仓单号", position = 2)
    @JSONField(ordinal = 2)
    private String warehouseNo;

    @ApiModelProperty(value = "提货状态(1未提货 2正在提货 3已提货 4已到仓)", position = 3)
    @JSONField(ordinal = 3)
    private String pickStatus;

    @ApiModelProperty(value = "提货状态描述", position = 4)
    @JSONField(ordinal = 4)
    private String pickStatusDescribes;

}
