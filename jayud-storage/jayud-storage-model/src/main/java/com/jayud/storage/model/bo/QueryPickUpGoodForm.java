package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 拣货订单订单商品列表查询条件
 */

@Data
public class QueryPickUpGoodForm extends BasePageForm{

    @ApiModelProperty(value = "订单号")
    private String orderNo;
}
