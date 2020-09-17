package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class InputSubOrderCustomsForm extends InputOrderForm{

    @ApiModelProperty(value = "报关子订单",required = true)
    @NotEmpty(message = "orderNo is required")
    private String orderNo;

    @ApiModelProperty(value = "报关抬头",required = true)
    @NotEmpty(message = "title is required")
    private String title;

    @ApiModelProperty(value = "结算单位code",required = true)
    @NotEmpty(message = "unitCode is required")
    private String unitCode;

    @ApiModelProperty(value = "结算单位",required = true)
    @NotEmpty(message = "unitAccount is required")
    private String unitAccount;

    @ApiModelProperty(value = "附件,多个逗号分隔",required = true)
    @NotEmpty(message = "description is required")
    private String description;

}
