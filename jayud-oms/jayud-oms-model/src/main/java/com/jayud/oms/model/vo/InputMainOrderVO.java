package com.jayud.oms.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class InputMainOrderVO {

    @ApiModelProperty(value = "主订单ID")
    private Long orderId;

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户code")
    private String customerCode;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

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

    @ApiModelProperty(value = "接单法人id")
    private Long legalEntityId;

    @ApiModelProperty(value = "业务所属部门")
    private Long bizBelongDepart;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "业务类型描述")
    private String bizDesc;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "已选中得服务")
    private String selectedServer;

    @ApiModelProperty(value = "产品类型 如CBG:纯报关")
    private String classCode;

    @ApiModelProperty(value = "结算单位code")
    private String unitCode;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "报关资料是否齐全 1-齐全 0-不齐全")
    private String isDataAll;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "主订单备注")
    private String remarks;

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "订单来源")
    private String orderSource;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo.... 参照CreateUserTypeEnum)")
    private Integer createUserType;

    @ApiModelProperty(value = "创建人的类型名称")
    private String createUserTypeName;

}
