package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 未出账单
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnbilledVO {

    @ApiModelProperty("法人主体id")
    private Long legalId;

    @ApiModelProperty("录用费用结算单位")
    private String customerCode;

    @ApiModelProperty("应收金额")
    private BigDecimal changeAmount;

    @ApiModelProperty("主订单")
    private String mainOrderNo;

    @ApiModelProperty("子订单")
    private String orderNo;



}
