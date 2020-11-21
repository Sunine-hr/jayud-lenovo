package com.jayud.finance.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EditCurrencyRateForm {

    @ApiModelProperty(value = "ID",required = true)
    private Long id;

    /*@ApiModelProperty(value = "右边兑换币种",required = true)
    @NotNull(message = "ocid is required")
    private Long ocid;

    @ApiModelProperty(value = "左边原始币种",required = true)
    @NotNull(message = "dcid is required")
    private Long dcid;*/

    @ApiModelProperty(value = "汇率",required = true)
    @NotNull(message = "exchangeRate is required")
    private BigDecimal exchangeRate;

   /* @ApiModelProperty(value = "XXXX-XX",required = true)
    @NotEmpty(message = "month is required")
    private String month;*/

    @ApiModelProperty(value = "有效期开始时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate beginValidDate;

    @ApiModelProperty(value = "有效期结束时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate endValidDate;

}
