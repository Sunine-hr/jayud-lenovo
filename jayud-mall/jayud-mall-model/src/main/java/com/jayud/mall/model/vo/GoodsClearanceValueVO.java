package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsClearanceValueVO {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "申报类型(2 清关)")
    private Integer type;

    @ApiModelProperty(value = "国家代码(country code)")
    private String countryCode;

    @ApiModelProperty(value = "国家名称(country name)")
    private String countryName;

    @ApiModelProperty(value = "申报价值")
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "(currency_info currency_code)")
    private String declaredCurrency;

}
