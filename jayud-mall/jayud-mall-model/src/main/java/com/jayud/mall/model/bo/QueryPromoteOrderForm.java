package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPromoteOrderForm extends BasePageForm{

    @ApiModelProperty(value = "公司id/渠道id(promote_company company_id)")
    private Integer companyId;

    @ApiModelProperty(value = "公司名称/渠道公司名称(promote_company company_name)")
    private String companyName;

    @ApiModelProperty(value = "(客户)公司名")
    private String clientCompanyName;

    @ApiModelProperty(value = "(客户)联系人")
    private String clientContacts;

}
