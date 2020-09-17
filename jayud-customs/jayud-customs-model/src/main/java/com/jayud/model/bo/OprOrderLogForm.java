package com.jayud.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OprOrderLogForm {

    @ApiModelProperty(value = "主订单")
    private String mainOrderNo;
    @ApiModelProperty(value = "子订单")
    private String orderNo;
    @ApiModelProperty(value = "描述")
    private String remarks;
    @ApiModelProperty(value = "操作人")
    private String optUname;
}
