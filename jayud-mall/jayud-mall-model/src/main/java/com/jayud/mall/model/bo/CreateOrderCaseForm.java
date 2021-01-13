package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "创建箱号的参数")
public class CreateOrderCaseForm {

    //总箱数
    @ApiModelProperty(value = "总箱数", position = 1)
    @JSONField(ordinal = 1)
    private Integer cartons;

    //每箱重量
    @ApiModelProperty(value = "每箱重量", position = 2)
    @JSONField(ordinal = 2)
    private BigDecimal weight;

    //长
    @ApiModelProperty(value = "长", position = 3)
    @JSONField(ordinal = 3)
    private BigDecimal length;

    //宽
    @ApiModelProperty(value = "宽", position = 4)
    @JSONField(ordinal = 4)
    private BigDecimal width;

    //高
    @ApiModelProperty(value = "高", position = 5)
    @JSONField(ordinal = 5)
    private BigDecimal height;

}
