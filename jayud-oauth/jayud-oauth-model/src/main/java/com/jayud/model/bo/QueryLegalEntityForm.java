package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryLegalEntityForm extends BasePageForm{

    @ApiModelProperty("法人主体")
    private String legalName;

}
