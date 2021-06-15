package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BtchCounterOrderInfoForm {

    @ApiModelProperty(value = "柜子清单信息表(counter_list_info id)")
    private Long counterListInfoId;

    @ApiModelProperty(value = "订单id(order_info id) list")
    private List<Long> orderIds;

}
