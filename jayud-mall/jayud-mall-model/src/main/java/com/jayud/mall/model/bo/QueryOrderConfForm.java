package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryOrderConfForm extends BasePageForm {

    @ApiModelProperty(value = "配载单号")
    private String orderNo;

    @ApiModelProperty(value = "目的国家(harbour_info id_code)")
    private String harbourCode;

}
