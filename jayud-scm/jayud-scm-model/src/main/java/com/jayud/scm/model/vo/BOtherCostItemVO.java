package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 费用名称表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Data
public class BOtherCostItemVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "费用名称代码")
    private String itemCode;

    @ApiModelProperty(value = "费用名称")
    private String itemName;

    @ApiModelProperty(value = "最高承担金额")
    private BigDecimal itemLimitAmt;

    @ApiModelProperty(value = "报价")
    private BigDecimal itemOfferAmt;

    @ApiModelProperty(value = "自营税点")
    private BigDecimal itemSalesTax;

    @ApiModelProperty(value = "代理税点")
    private BigDecimal itemProxyTax;

    @ApiModelProperty(value = "承担方（客户承担，供应商，我司）")
    private String cdf;

    @ApiModelProperty(value = "备注")
    private String remark;
}
