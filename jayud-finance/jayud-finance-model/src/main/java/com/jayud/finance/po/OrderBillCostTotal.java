package com.jayud.finance.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 出账账单费用合计
 * </p>
 *
 * @author chuanmei
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OrderBillCostTotal对象", description="")
public class OrderBillCostTotal extends Model<OrderBillCostTotal> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "出账订单维度的费用统计ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "结算币种")
    private String currencyCode;

    @ApiModelProperty(value = "费用ID")
    private Long costId;

    @ApiModelProperty(value = "费用项")
    private String costInfoName;

    @ApiModelProperty(value = "金额,根据结算币种计算的金额，以费用项为维度")
    private BigDecimal money;

    @ApiModelProperty(value = "本币金额")
    private BigDecimal localMoney;

    @ApiModelProperty(value = "1-有效 0-无效")
    private String status;

    @ApiModelProperty(value = "1-应付 2-应收")
    private String moneyType;

    @ApiModelProperty(value = "当前币种")
    private String currentCurrencyCode;

    @ApiModelProperty(value = "结算汇率")
    private BigDecimal exchangeRate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
