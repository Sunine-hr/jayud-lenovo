package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomsBaseValueForm {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "类型(1报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "海关基础资料id(customs_data id customs_clearance id)")
    private Long customsId;

    @ApiModelProperty(value = "国家代码(country code)")
    private String countryCode;

    @ApiModelProperty(value = "国家名称(country name)")
    private String countryName;

    @ApiModelProperty(value = "申报价值")
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币单位(currency_info currency_code)")
    private String declaredCurrency;

}
