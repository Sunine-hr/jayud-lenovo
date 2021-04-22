package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PromoteCompanyIdForm {

    @ApiModelProperty(value = "公司id/渠道id")
    @NotNull(message = "公司id/渠道id不能为空")
    private Integer companyId;


}
