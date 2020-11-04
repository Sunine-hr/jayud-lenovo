package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询客户Form")
public class QueryCustomerForm extends BasePageForm{

    @ApiModelProperty(value = "中文名，联系人")
    private String nameCn;

    @ApiModelProperty(value = "公司名")
    private String company;

}
