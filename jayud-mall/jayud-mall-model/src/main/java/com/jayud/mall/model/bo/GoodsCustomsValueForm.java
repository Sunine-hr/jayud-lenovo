package com.jayud.mall.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCustomsValueForm {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Integer goodId;

    @ApiModelProperty(value = "申报类型(1 报关)")
    private Integer type;

    @ApiModelProperty(value = "国家代码(country code)")
    private String countryCode;

    @ApiModelProperty(value = "国家名称(country name)")
    private String countryName;

    @ApiModelProperty(value = "申报价值")
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币单位(currency_info currency_code)")
    private String declaredCurrency;

}
