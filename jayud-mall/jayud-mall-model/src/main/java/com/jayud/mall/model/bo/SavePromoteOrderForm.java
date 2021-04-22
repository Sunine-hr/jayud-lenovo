package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SavePromoteOrderForm {

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "公司id/渠道id(promote_company company_id)")
    private Integer companyId;

    @ApiModelProperty(value = "(客户)公司名")
    private String clientCompanyName;

    @ApiModelProperty(value = "(客户)联系人")
    private String clientContacts;

    @ApiModelProperty(value = "(客户)联系电话")
    private String clientPhone;

    @ApiModelProperty(value = "(客户)公司地址")
    private String clientCompanyAddress;

    @ApiModelProperty(value = "(客户)运营平台")
    private String clientManagePlatform;

}
