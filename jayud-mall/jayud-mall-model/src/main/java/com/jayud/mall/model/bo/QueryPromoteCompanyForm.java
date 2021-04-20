package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPromoteCompanyForm extends BasePageForm {

    @ApiModelProperty(value = "公司id/渠道id")
    private Integer companyId;

    @ApiModelProperty(value = "公司名称/渠道公司名称")
    private String companyName;

    @ApiModelProperty(value = "是否仅查询公司，true 仅查询公司 false 仅查询渠道")
    private Boolean isQueryCompany;

}
