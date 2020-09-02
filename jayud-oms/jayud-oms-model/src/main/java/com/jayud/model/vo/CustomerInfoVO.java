package com.jayud.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerInfoVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "客户名")
    private String name;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;

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
    private Integer accountPeriod;

    @ApiModelProperty(value = "等级")
    private Integer estate;

    @ApiModelProperty(value = "对应部门code")
    private String departmentCode;

    @ApiModelProperty(value = "对应客服code")
    private String kuCode;

    @ApiModelProperty(value = "对应业务员code")
    private String ywCode;

}
