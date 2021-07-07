package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerForm {

    @ApiModelProperty(value = "主键id，自增")
    private Integer id;

    @ApiModelProperty(value = "公司名,客户名称")
    private String company;

    @ApiModelProperty(value = "用户名，即登录名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "营业执照url")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "结算方式id(clearing_way id)")
    private Integer clearingWay;

    @ApiModelProperty(value = "结算日期")
    private String clearingDate;

    @ApiModelProperty(value = "锁单日期")
    private String lockDate;

    @ApiModelProperty(value = "实际锁单日期")
    private String realDate;

    /*审核*/
    @ApiModelProperty(value = "审核状态(0待审核1审核通过2审核不通过，默认为0）")
    private Integer auditStatus;

    @ApiModelProperty(value = "业务员Id，system_user.id")
    private Integer salesmanId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private String operationTeamId;

    @ApiModelProperty(value = "备注")
    private String remark;


}
