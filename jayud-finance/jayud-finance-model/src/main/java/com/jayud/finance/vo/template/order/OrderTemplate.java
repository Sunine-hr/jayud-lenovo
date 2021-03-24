package com.jayud.finance.vo.template.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderTemplate {

//    @ApiModelProperty(value = "订单编号")
//    private String orderNo;
//
//    @ApiModelProperty(value = "子订单编号")
//    private String subOrderNo;

    @ApiModelProperty(value = "业务类型")
    private String bizCodeDesc;

    @ApiModelProperty(value = "创建日期")
    private String createdTimeStr;

    @ApiModelProperty(value = "结算单位")
    private String unitAccount;
}
