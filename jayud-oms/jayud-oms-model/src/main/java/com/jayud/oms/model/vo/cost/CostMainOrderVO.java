package com.jayud.oms.model.vo.cost;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CostMainOrderVO {

    @ApiModelProperty(value = "主订单号")
    private String orderNo;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

//    @ApiModelProperty(value = "业务员")
//    private String bizUname;

//    @ApiModelProperty(value = "合同编号")
//    private String contractNo;

    @ApiModelProperty(value = "接单法人")
    private String legalName;

//    @ApiModelProperty(value = "业务所属部门")
//    private Long bizBelongDepart;

//    @ApiModelProperty(value = "客户参考号")
//    private String referenceNo;

//    @ApiModelProperty(value = "业务类型")
//    private String bizCode;

//    @ApiModelProperty(value = "业务类型描述")
//    private String bizDesc;

    @ApiModelProperty(value = "已选中得服务")
    private String selectedServer;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

//    @ApiModelProperty(value = "报关资料是否齐全 1-齐全 0-不齐全")
//    private String isDataAll;


}
