package com.jayud.Inlandtransport.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class InputMainOrderIfForm {

    @ApiModelProperty(value = "主订单ID,编辑时必传")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户code",required = true)
    private String customerCode;

    @ApiModelProperty(value = "客户",required = true)
    private String customerName;

    @ApiModelProperty(value = "业务员ID",required = true)
    private Long bizUid;

    @ApiModelProperty(value = "业务员",required = true)
    private String bizUname;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "接单法人",required = true)
    private String legalName;

    @ApiModelProperty(value = "接单法人ID", required = true)
    private Long legalEntityId;

    @ApiModelProperty(value = "业务所属部门",required = true)
    private Long bizBelongDepart;

    @ApiModelProperty(value = "客户参考号")
    private String referenceNo;

    @ApiModelProperty(value = "业务类型",required = true)
    private String bizCode;

    @ApiModelProperty(value = "产品类型 如CBG:纯报关",required = true)
    private String classCode;

    @ApiModelProperty(value = "已选中得服务",required = true)
    private String selectedServer;

    @ApiModelProperty(value = "结算单位code",required = true)
    private String unitCode;

    @ApiModelProperty(value = "结算单位",required = true)
    private String unitAccount;

    @ApiModelProperty(value = "报关资料是否齐全 1-齐全 0-不齐全",required = true)
    private String isDataAll;

    @ApiModelProperty(value = "操作指令,前台忽略")
    private String cmd;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "创建人的类型(0:本系统,1:vivo,2smc..... 参照CreateUserTypeEnum)")
    private Integer createUserType;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;
}
