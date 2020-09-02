package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 客户信息界面
 */
@Data
public class AddCustomerInfoForm {

    @ApiModelProperty(value = "客户名")
    @NotEmpty(message = "name is required")
    private String name;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

    @ApiModelProperty(value = "客户代码")
    @NotEmpty(message = "idCode is required")
    private String idCode;

    @ApiModelProperty(value = "客户类型(1同行 2电商 3货代)")
    @NotEmpty(message = "types is required")
    @Pattern(regexp = "1|2|3",message = "types requires '1' or '2' or '3' only")
    private Integer types;

    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "contact is required")
    private String contact;

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "phone is required")
    private String phone;

    @ApiModelProperty(value = "地址")
    @NotEmpty(message = "address is required")
    private String address;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "法人主体")
    @NotEmpty(message = "legalEntity is required")
    private String legalEntity;

    @ApiModelProperty(value = "发票抬头")
    @NotEmpty(message = "invoiceCode is required")
    private String invoiceCode;

    @ApiModelProperty(value = "纳税号")
    @NotEmpty(message = "tfn is required")
    private String tfn;

    @ApiModelProperty(value = "是否有合同(0否 1是)")
    @NotEmpty(message = "ifContract is required")
    @Pattern(regexp = "0|1",message = "ifContract requires '0' or '1' only")
    private String ifContract;

    @ApiModelProperty(value = "结算类型(1票结 2月结 3周结)")
    @NotEmpty(message = "settlementType is required")
    @Pattern(regexp = "1|2|3",message = "settlementType requires '1' or '2' or '3' only")
    private Integer settlementType;

    @ApiModelProperty(value = "账期")
    private Integer accountPeriod;

    @ApiModelProperty(value = "等级")
    private Integer estate;

    @ApiModelProperty(value = "对应部门code")
    @NotEmpty(message = "departmentCode is required")
    private String departmentCode;

    @ApiModelProperty(value = "对应客服code")
    private String kuCode;

    @ApiModelProperty(value = "对应业务员code")
    @NotEmpty(message = "ywCode is required")
    private String ywCode;
}
