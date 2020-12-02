package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 币种
 * </p>
 *
 * @author chuanmei
 * @since 2020-09-19
 */
@Data
public class CurrencyInfoVO {


    @ApiModelProperty(value = "币种代码")
    private String currencyCode;

    @ApiModelProperty(value = "币种名称")
    private String currencyName;

    @ApiModelProperty(value = "他币与人民币的汇率")
    private BigDecimal exchangeRate;


}
