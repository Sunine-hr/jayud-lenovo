package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsServiceCostVO {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "服务id(service_group id)")
    private Long serviceId;

    @ApiModelProperty(value = "商品id(customer_goods id)")
    private Long goodId;

    @ApiModelProperty(value = "客户ID(customer id)")
    private Long customerId;

    @ApiModelProperty(value = "品名(中文名)(customer_goods name_cn)")
    private String nameCn;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "单位(1公斤 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "服务名称(service_group code_name)")
    private String serviceName;

    @ApiModelProperty(value = "客户名称(customer company)")
    private String customerName;

    @ApiModelProperty(value = "是否需要附加费(1需要 2不需要)")
    private String isNeedFee;

}
