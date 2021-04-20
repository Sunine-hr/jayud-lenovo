package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PromoteCompanyIdForm {

    @ApiModelProperty(value = "公司id/渠道id")
    private Integer companyId;


}
