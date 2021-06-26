package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderServiceReceivableForm {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "订单服务id(order_service id)")
    private Long qie;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)")
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)")
    private String costName;

    @ApiModelProperty(value = "规格代码(quotation_type code;shipping_area warehouse_code)")
    private String specificationCode;

    @ApiModelProperty(value = "规格名称(quotation_type name;shipping_area warehouse_name)")
    private String specificationName;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公斤 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "数量来源(1计费重 2固定 3柜 4方)")
    private Integer source;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "描述")
    private String remarks;

}
