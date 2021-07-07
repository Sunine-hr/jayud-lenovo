package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderServiceIdForm {

    @ApiModelProperty(value = "主键id，自动增长(order_service id)")
    @NotNull(message = "订单服务id不能为空")
    private Long id;


}
