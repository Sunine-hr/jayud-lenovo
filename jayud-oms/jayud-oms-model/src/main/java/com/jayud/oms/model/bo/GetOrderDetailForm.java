package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class GetOrderDetailForm {

    @ApiModelProperty(value = "主订单号", required = true)
    @NotNull(message = "mainOrderId is required")
    private Long mainOrderId;

    @ApiModelProperty(value = "操作类型", required = true)
    @NotEmpty(message = "classCode is required")
    private String classCode;

//    @ApiModelProperty(value = "是否主订单入口", required = true)
//    private Boolean isMainEntrance;
//
//    public void setMainEntrance(String isMainEntrance) {
//        this.isMainEntrance = "main".equals(isMainEntrance);
//    }
}
