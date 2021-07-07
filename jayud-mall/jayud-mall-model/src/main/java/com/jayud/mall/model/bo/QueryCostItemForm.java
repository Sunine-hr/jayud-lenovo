package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCostItemForm extends BasePageForm {


    @ApiModelProperty(value = "辨认费用类型(1应收费用 2应付费用)")
    private String identifying;

    @ApiModelProperty(value = "费用代码")
    private String costCode;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

}
