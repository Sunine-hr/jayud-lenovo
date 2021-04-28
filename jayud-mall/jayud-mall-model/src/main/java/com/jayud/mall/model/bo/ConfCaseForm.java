package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfCaseForm extends BasePageForm{

    @ApiModelProperty(value = "提单id")
    @NotNull(message = "提单id必填")
    private Long billId;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

}
