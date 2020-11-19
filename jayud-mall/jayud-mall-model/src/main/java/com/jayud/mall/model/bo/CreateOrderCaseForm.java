package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "创建箱号的参数")
public class CreateOrderCaseForm {

    //总箱数
    @ApiModelProperty(value = "总箱数")
    private Integer cartons;

    //每箱重量
    @ApiModelProperty(value = "每箱重量")
    private BigDecimal weight;

    //长
    @ApiModelProperty(value = "长")
    private BigDecimal length;

    //宽
    @ApiModelProperty(value = "宽")
    private BigDecimal width;

    //高
    @ApiModelProperty(value = "高")
    private BigDecimal height;

}
