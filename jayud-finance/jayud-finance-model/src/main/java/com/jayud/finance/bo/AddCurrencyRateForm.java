package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AddCurrencyRateForm {

    @ApiModelProperty(value = "ID,修改时需")
    private Long id;

    @ApiModelProperty(value = "他币",required = true)
    @NotNull(message = "ocid is required")
    private Long ocid;

    @ApiModelProperty(value = "汇率",required = true)
    @NotNull(message = "exchangeRate is required")
    private BigDecimal exchangeRate;

}
