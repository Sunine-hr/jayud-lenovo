package com.jayud.storage.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 库存表
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
@Data
public class StockLocationNumberVO {

    @ApiModelProperty(value = "商品名称")
    private String goodName;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "sku")
    private String sku;

    @ApiModelProperty(value = "可用库存")
    private Integer availableStock;

    @ApiModelProperty(value = "锁定库存")
    private Integer lockStock;

}