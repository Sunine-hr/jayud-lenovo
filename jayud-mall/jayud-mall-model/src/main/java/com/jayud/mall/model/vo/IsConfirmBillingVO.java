package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IsConfirmBillingVO {

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "状态标志-是否确认计费重(1已确认 2未确认)" ,notes = "OrderEnum.IS_CONFIRM_BILLING")
    private String statusFlag;

}
