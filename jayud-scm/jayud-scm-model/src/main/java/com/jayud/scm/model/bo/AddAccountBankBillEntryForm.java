package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 水单明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class AddAccountBankBillEntryForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "水单主表ID")
    private Integer bankBillId;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单明细ID")
    private Integer orderEntryId;

    @ApiModelProperty(value = "应收款号码")
    private String invoiceNo;

    @ApiModelProperty(value = "应收款主表ID")
    private Integer invoiceId;

    @ApiModelProperty(value = "应收款明细ID")
    private Integer invoiceEntryId;

    @ApiModelProperty(value = "费用类别")
    private String feeAlias;

    @ApiModelProperty(value = "核销应收金额")
    private BigDecimal acMoney;

    @ApiModelProperty(value = "扣减代理费")
    private BigDecimal proxyMoney;

    @ApiModelProperty(value = "手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "备注")
    private String remark;

}
