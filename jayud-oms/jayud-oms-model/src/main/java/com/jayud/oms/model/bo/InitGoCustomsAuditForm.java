package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class InitGoCustomsAuditForm {

    @ApiModelProperty(value = "主订单号",required = true)
    @NotEmpty(message = "orderNo is required")
    private String orderNo;

    @ApiModelProperty(value = "已选中得服务")
    @NotEmpty(message = "selectedServer is required")
    private String selectedServer;

}
