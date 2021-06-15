package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderInfoQueryForm {

    @ApiModelProperty(value = "配载id(order_conf id)")
    //@NotNull(message = "配载id不能为空")
    private Long orderConfId;

    @ApiModelProperty(value = "柜子清单信息表id(counter_list_info id)")
    @NotNull(message = "柜子清单信息表id不能为空")
    private Long counterListInfoId;

    @ApiModelProperty(value = "过滤的订单id")
    private List<Long> filterOrderIds;

    @ApiModelProperty(value = "订单号")
    private String orderNo;



}
