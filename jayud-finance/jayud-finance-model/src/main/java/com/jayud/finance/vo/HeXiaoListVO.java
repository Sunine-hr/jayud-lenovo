package com.jayud.finance.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 核销列表
 */
@Data
public class HeXiaoListVO {

    @ApiModelProperty(value = "核销ID")
    private Long heXiaoId;

    @ApiModelProperty(value = "对账单编号")
    private String billNo;

    @ApiModelProperty(value = "实收金额")
    private BigDecimal realReceiveMoney;

    @ApiModelProperty(value = "币种")
    private String currencyCode;

    @ApiModelProperty(value = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "折合金额")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "核销方式")
    private String oprMode;

    @ApiModelProperty(value = "实际收款实际")
    private LocalDateTime realReceiveTime;


}
