package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderServiceVO {

    @ApiModelProperty(value = "主键id，自动增长")
    private Long id;

    @ApiModelProperty(value = "订单id(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "订单号(order_info order_no)")
    private String orderNo;

    @ApiModelProperty(value = "服务id(service_item id)")
    private Long serviceId;

    @ApiModelProperty(value = "服务名称(service_item name)")
    private String serviceName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //订单服务对应应收费用
    @ApiModelProperty(value = "订单服务对应应收费用 list")
    private List<OrderServiceReceivableVO> orderServiceReceivableList;

    //订单服务对应应付费用
    @ApiModelProperty(value = "订单服务对应应付费用 list")
    private List<OrderServiceWithVO> orderServiceWithList;

}
