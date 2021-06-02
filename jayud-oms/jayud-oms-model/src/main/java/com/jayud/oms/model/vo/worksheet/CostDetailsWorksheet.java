package com.jayud.oms.model.vo.worksheet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 中港工作表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CostDetailsWorksheet {

    @ApiModelProperty(value = "应收对象")
    private String reCustomer;

    @ApiModelProperty(value = "应收收费明细")
    private String reCost;

    @ApiModelProperty(value = "币别")
    private String reCurrency;

    @ApiModelProperty(value = "应收金额")
    private BigDecimal reAmount;

    @ApiModelProperty(value = "付款对象")
    private String payCustomer;

    @ApiModelProperty(value = "应付费用明细")
    private String payCost;

    @ApiModelProperty(value = "应付币别")
    private String payCurrency;

    @ApiModelProperty(value = "应付金额")
    private BigDecimal payAmount;

}
