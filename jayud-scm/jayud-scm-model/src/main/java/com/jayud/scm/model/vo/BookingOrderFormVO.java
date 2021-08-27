package com.jayud.scm.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 委托订单主表
 */
@Data
public class BookingOrderFormVO {

    @ApiModelProperty(value = "自动id")
    private Integer id;

    @ApiModelProperty(value = "委托单号")
    private String bookingNo;

    @ApiModelProperty(value = "起运地（默认深圳、香港）")
    private String origin;

    @ApiModelProperty(value = "目的地")
    private String destination;

    @ApiModelProperty(value = "客户合同号")
    private String contractNo;

    @ApiModelProperty(value = "供应商名称（可以使用，实际供应商在明细表）")
    private String supplierName;

    @ApiModelProperty(value = "客户名称、利润中心")
    private String customerName;

    @ApiModelProperty(value = "运保费金额")
    private BigDecimal totalCipPrice;

    @ApiModelProperty(value = "总价格(=qty*price)")
    private BigDecimal totalMoney;

    @ApiModelProperty(value = "报关总价(=hg_price*qty)")
    private BigDecimal totalHgMoney;

    @ApiModelProperty(value = "件数")
    private BigDecimal packages;

    @ApiModelProperty(value = "毛重")
    private BigDecimal gw;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

}
