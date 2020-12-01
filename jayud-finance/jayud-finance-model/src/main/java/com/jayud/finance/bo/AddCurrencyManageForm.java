package com.jayud.finance.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *新增汇率
 */
@Data
public class AddCurrencyManageForm {

    @ApiModelProperty(value = "XXXX-XX",required = true)
    @NotEmpty(message = "month is required")
    private String month;

    @Valid
    @ApiModelProperty(value = "汇率集合",required = true)
    private List<AddCurrencyRateForm> rateFormList;

    @ApiModelProperty(value = "登录用户",required = true)
    private String loginUserName;

}
