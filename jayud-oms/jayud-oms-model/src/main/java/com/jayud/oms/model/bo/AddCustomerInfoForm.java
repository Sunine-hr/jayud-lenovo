package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 新增客户信息界面
 */
@Data
public class AddCustomerInfoForm {

    @ApiModelProperty(value = "主键ID,修改时必传")
    private Integer id;

    @ApiModelProperty(value = "客户名称",required = true)
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "客户代码",required = true)
    @NotEmpty(message = "idCode is required")
    private String idCode;

    @ApiModelProperty(value = "结算单位",required = true)
    @NotEmpty(message = "unitAccount is required")
    private String unitAccount;

    @ApiModelProperty(value = "结算代码",required = true)
    @NotEmpty(message = "unitCode is required")
    private String unitCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)",required = true)
    @Pattern(regexp = "1|2|3",message = "types requires '1' or '2' or '3' only")
    private Integer types;

    @ApiModelProperty(value = "联系人",required = true)
    @NotEmpty(message = "contact is required")
    private String contact;

    @ApiModelProperty(value = "联系电话",required = true)
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "地址",required = true)
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "法人主体",required = true)
    @NotEmpty(message = "legalEntity is required")
    private String legalEntity;

    @ApiModelProperty(value = "发票抬头",required = true)
    @NotEmpty(message = "invoiceCode is required")
    private String invoiceCode;

    @ApiModelProperty(value = "纳税号",required = true)
    @NotEmpty(message = "tfn is required")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)",required = true)
    @Pattern(regexp = "0|1",message = "ifContract requires '0' or '1' only")
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

    @ApiModelProperty(value = "接单部门ID",required = true)
    @NotEmpty(message = "departmentId is required")
    private String departmentId;

    @ApiModelProperty(value = "接单部门",required = true)
    @NotEmpty(message = "departmentName is required")
    private String departmentName;

    @ApiModelProperty(value = "接单客服ID")
    private Long kuId;

    @ApiModelProperty(value = "接单客服")
    private String kuName;

    @ApiModelProperty(value = "业务员ID",required = true)
    @NotEmpty(message = "ywId is required")
    private Long ywId;

    @ApiModelProperty(value = "业务员",required = true)
    @NotEmpty(message = "ywName is required")
    private String ywName;

}
