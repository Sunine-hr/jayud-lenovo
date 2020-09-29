package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class InputMainOrderForm {


    @ApiModelProperty(value = "主订单ID,编辑时必传")
    private Long orderId;

    @ApiModelProperty(value = "客户code",required = true)
    @NotEmpty(message = "customerCode is required")
    private String customerCode;

    @ApiModelProperty(value = "客户",required = true)
    @NotEmpty(message = "customerName is required")
    private String customerName;

    @ApiModelProperty(value = "业务员ID",required = true)
    @NotEmpty(message = "bizUid is required")
    private Long bizUid;

    @ApiModelProperty(value = "业务员",required = true)
    @NotEmpty(message = "bizUname is required")
    private String bizUname;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "接单法人",required = true)
    @NotEmpty(message = "legalName is required")
    private String legalName;

    @ApiModelProperty(value = "业务所属部门",required = true)
    @NotEmpty(message = "bizBelongDepart is required")
    private Long bizBelongDepart;

    @ApiModelProperty(value = "客户参考号",required = true)
    @NotEmpty(message = "referenceNo is required")
    private String referenceNo;

    @ApiModelProperty(value = "业务类型",required = true)
    private String bizCode;

    @ApiModelProperty(value = "产品类型 CBG:纯报关",required = true)
    private String classCode;

    @ApiModelProperty(value = "操作指令:cmd=preSubmit or submit",required = true)
    private String cmd;


}
