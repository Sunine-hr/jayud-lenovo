package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IsAuditOrderVO {

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    //IS_AUDIT_ORDER("is_audit_order", "是否审核单据(1已审单 2未审单)"),
    @ApiModelProperty(value = "状态标志-是否审核单据(1已审单 2未审单)" ,notes = "OrderEnum.IS_AUDIT_ORDER")
    private String statusFlag;

}
