package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询客户信息界面
 */
@Data
public class QueryCustomerInfoForm extends BasePageForm{

    @ApiModelProperty(value = "客户名")
    private String name;

    @ApiModelProperty(value = "操作指令，cmd=kf or cw or zjb",required = true)
    private String cmd;

    @ApiModelProperty(value = "客户代码")
    private String idCode;

    @ApiModelProperty(value = "业务员ID")
    private Long ywId;

    @ApiModelProperty(value = "联系人")
    private String contact;

    @ApiModelProperty(value = "当前登录用户")
    private String loginUserName;

    @ApiModelProperty(value = "审核状态 ( 0-待客服审核 1-待财务审核 2-待总经办审核 10-通过 11-拒绝)")
    private Integer auditStatus;

}
