package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryGoodForm {

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "客户名称")
    private String customerName;
}
