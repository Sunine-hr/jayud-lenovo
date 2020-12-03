package com.jayud.oms.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerInfoVO {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "一级客户名称")
    private String name;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "法人主体")
    private String legalEntity;

    @ApiModelProperty(value = "发票抬头")
    private String invoiceCode;

    @ApiModelProperty(value = "纳税号")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)")
    private String ifContract;

    @ApiModelProperty(value = "结算类型(1票结 2月结 3周结)")
    private Integer settlementType;

    @ApiModelProperty(value = "账期")
    private String accountPeriod;

    @ApiModelProperty(value = "税票种类")
    private String taxType;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "等级")
    private Integer estate;

    @ApiModelProperty(value = "接单部门ID")
    private Long departmentId;

    @ApiModelProperty(value = "接单部门")
    private String departmentName;

    @ApiModelProperty(value = "接单客服ID")
    private Long kuId;

    @ApiModelProperty(value = "业务员ID")
    private Long ywId;

    @ApiModelProperty(value = "接单客服")
    private String kuName;

    @ApiModelProperty(value = "业务员")
    private String ywName;

    @ApiModelProperty(value = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核状态描述")
    private String auditStatusDesc;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private String createdTimeStr;

    @ApiModelProperty(value = "审核意见")
    private String auditComment;

    @ApiModelProperty(value = "更新时间")
    private String updatedTimeStr;
}
