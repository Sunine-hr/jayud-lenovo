package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryLegalEntityForm extends BasePageForm {

    @ApiModelProperty(value = "法人主体")
    private String legalName;

    @ApiModelProperty(value = "主体编号")
    private String legalCode;

}
