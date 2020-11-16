package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderInfoForm {

    @ApiModelProperty(value = "订单ID，由系统生成")
    private Long id;

}
