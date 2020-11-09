package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报关资料表")
public class QueryCustomsDataForm extends BasePageForm {

    @ApiModelProperty(value = "IDcode，报关ID")
    private String idCode;

    @ApiModelProperty(value = "报关英文品名")
    private String enName;

    @ApiModelProperty(value = "报关HSCODE")
    private String hsCode;



}
