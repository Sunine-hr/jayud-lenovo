package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 单一窗口数据
 * </p>
 *
 * @author LLJ
 * @since 2021-08-21
 */
@Data
public class SingleWindowData {

    @ApiModelProperty(value = "自动ID")
    private Integer billId;

    @ApiModelProperty(value = "报关合同号")
    private String billNo;

    @ApiModelProperty(value = "拼单日期")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "保费币别")
    @JsonProperty(value = "bFeeCurrency")
    private String bFeeCurrency;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "供应商id")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "商品型号")
    private String itemModel;

    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "品牌")
    private String itemBrand;

    @ApiModelProperty(value = "产地")
    private String itemOrigin;

}
