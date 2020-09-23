package com.jayud.customs.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class InputOrderCustomsForm {

    @ApiModelProperty(value = "通关口岸code",required = true)
    @NotEmpty(message = "portCode is required")
    private String portCode;

    @ApiModelProperty(value = "通关口岸",required = true)
    @NotEmpty(message = "portName is required")
    private String portName;

    @ApiModelProperty(value = "货物流向",required = true)
    @NotEmpty(message = "goodsType is required")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址")
    private String cntrPic;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "子订单",required = true)
    @NotEmpty(message = "subOrders is required")
    private List<InputSubOrderCustomsForm> subOrders;


}
