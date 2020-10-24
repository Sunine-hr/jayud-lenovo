package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class TmsChangeStatusForm {

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否需要录入费用")
    private Boolean needInputCost;


}
