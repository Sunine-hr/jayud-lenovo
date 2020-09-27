package com.jayud.oms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询合同信息界面
 */
@Data
public class QueryOrderStatusForm extends BasePageForm{

    @ApiModelProperty(value = "主订单ID")
    private Long mainOrderId;

    @ApiModelProperty(value = "业务类型")
    private String bizCode;

    @ApiModelProperty(value = "子订单ID,不用传")
    private Long orderId;

}
