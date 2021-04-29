package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CounterCaseInfoExcelVO {

    @ApiModelProperty(value = "清单名称(counter_list_info file_name)")
    private String bName;

    @ApiModelProperty(value = "提单号(ocean_bill order_id)")
    private String billNo;

    @ApiModelProperty(value = "箱号(order_case carton_no)")
    private String cartonNo;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

}
