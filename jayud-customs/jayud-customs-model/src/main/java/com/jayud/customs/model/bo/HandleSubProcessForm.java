package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class HandleSubProcessForm {

    @ApiModelProperty(value = "子订单ID",required = true)
    @NotEmpty(message = "orderId is required")
    private Long orderId;

    @ApiModelProperty(value = "产品类型",required = true)
    @NotEmpty(message = "classCode is required")
    private String classCode;

}
