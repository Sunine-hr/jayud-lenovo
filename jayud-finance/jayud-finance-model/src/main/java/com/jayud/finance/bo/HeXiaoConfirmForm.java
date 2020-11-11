package com.jayud.finance.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 核销
 */
@Data
public class HeXiaoConfirmForm {

    @ApiModelProperty(value = "核销ID")
    private Long id;

    @ApiModelProperty(value = "对账单编号",required = true)
    @NotEmpty(message = "billNo is required")
    private String billNo;

    @ApiModelProperty(value = "实收金额",required = true)
    @NotNull(message = "realReceiveMoney is required")
    private BigDecimal realReceiveMoney;

    @ApiModelProperty(value = "币种",required = true)
    @NotEmpty(message = "currencyCode is required")
    private String currencyCode;

    @ApiModelProperty(value = "汇率",required = true)
    @NotNull(message = "exchangeRate is required")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "折合金额",required = true)
    @NotNull(message = "discountMoney is required")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "核销方式",required = true)
    @NotEmpty(message = "oprMode is required")
    private String oprMode;

    @ApiModelProperty(value = "实际收款实际",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime realReceiveTime;


}
