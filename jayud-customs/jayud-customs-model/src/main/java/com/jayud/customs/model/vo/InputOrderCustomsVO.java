package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class InputOrderCustomsVO {

    @ApiModelProperty(value = "通关口岸code")
    private String portCode;

    @ApiModelProperty(value = "货物流向")
    private Integer goodsType;

    @ApiModelProperty(value = "柜号")
    private String cntrNo;

    @ApiModelProperty(value = "柜号上传附件地址")
    private String cntrPic;

    @ApiModelProperty(value = "六联单号")
    private String encode;

    @ApiModelProperty(value = "报关单数")
    private String number;

    @ApiModelProperty(value = "子订单")
    private List<InputSubOrderCustomsVO> subOrders;


}
