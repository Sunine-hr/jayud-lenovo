package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderStatusVO {

    @ApiModelProperty(value = "状态代码")
    private String code;

    @ApiModelProperty(value = "状态名称")
    private String name;

    @ApiModelProperty(value = "前端状态list")
    private List<OrderStatusVO> frontStatus;

    @ApiModelProperty(value = "后端状态list")
    private List<OrderStatusVO> afterStatus;

    public OrderStatusVO() {
    }

    public OrderStatusVO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
