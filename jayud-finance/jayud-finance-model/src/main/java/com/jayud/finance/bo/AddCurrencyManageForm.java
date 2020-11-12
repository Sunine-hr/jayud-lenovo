package com.jayud.finance.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 *新增汇率
 */
@Data
public class AddCurrencyManageForm {

    @ApiModelProperty(value = "XXXX-XX",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM")
    private LocalDate monthDate;

    @ApiModelProperty(value = "汇率集合",required = true)
    @NotNull(message = "rateFormList is required")
    private List<AddCurrencyRateForm> rateFormList;

    @ApiModelProperty(value = "有效期开始时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate beginValidDate;

    @ApiModelProperty(value = "有效期结束时间",required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate endValidDate;

}
