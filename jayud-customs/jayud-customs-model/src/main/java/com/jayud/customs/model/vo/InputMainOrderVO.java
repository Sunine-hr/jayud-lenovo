package com.jayud.customs.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class InputMainOrderVO {

    @ApiModelProperty(value = "主订单ID")
    private Long orderId;

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户code")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务员ID")
    private Long bizUid;

    @ApiModelProperty(value = "业务员")
    private String bizUname;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

    @ApiModelProperty(value = "业务所属部门")
    private Long bizBelongDepart;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

}
