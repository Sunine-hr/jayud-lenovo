package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderCaseQueryForm {

    @ApiModelProperty(value = "柜子清单信息表(counter_list_info id)")
    private Long counterListInfoId;

    @ApiModelProperty(value = "订单id(order_info id)")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @ApiModelProperty(value = "过滤 箱号id(order_case id) list")
    private List<Long> filterCaseIds;

    @ApiModelProperty(value = "箱号")
    private String cartonNo;

}
