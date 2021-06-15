package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInfoQueryForm {


    @ApiModelProperty(value = "柜子清单信息表id")
    private Long counterListInfoId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

}
