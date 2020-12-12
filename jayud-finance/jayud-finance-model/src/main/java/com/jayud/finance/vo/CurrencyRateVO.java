package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 汇率管理列表分页查询
 */
@Data
public class CurrencyRateVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "年月")
    private String monthStr;

    @ApiModelProperty(value = "年月")
    private String month;

    @ApiModelProperty(value = "兑换币种ID")
    private Long dcid;

    @ApiModelProperty(value = "原始币种ID")
    private Long ocid;

    @ApiModelProperty(value = "兑换币种")
    private String dCurrencyName;

    @ApiModelProperty(value = "原始币种")
    private String oCurrencyName;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;



}
