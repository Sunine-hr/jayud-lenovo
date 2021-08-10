package com.jayud.scm.model.bo;

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
import java.util.List;

/**
 * <p>
 * 结算方案条款
 * </p>
 *
 * @author LLJ
 * @since 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddFeeForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "客户ID")
    private Integer CustomerId;

    @ApiModelProperty(value = "结算方案ID")
    private Integer feeModelId;

    @ApiModelProperty(value = "代理方式(代理进口，自营进口，自营出口，代办退税)")
    private String deputyStyle;

    @ApiModelProperty(value = "交易方式（CIF，FOB）")
    private String incoterms;

    @ApiModelProperty(value = "付款方式")
    private String payStyle;

    @ApiModelProperty(value = "付款账期天数")
    private Integer payDay;

    @ApiModelProperty(value = "收款方式")
    private String reStyle;

    @ApiModelProperty(value = "收款账期天数")
    private Integer reDay;

    @ApiModelProperty(value = "结算方式")
    private String accountStyle;

    @ApiModelProperty(value = "额度类型(货款额度，税费额度)")
    private String quotaType;

    @ApiModelProperty(value = "产品类型")
    private String productClass;

    @ApiModelProperty(value = "代理费率")
    private BigDecimal proxyFee;

    @ApiModelProperty(value = "货款汇率类型")
    private String gRateType;

    @ApiModelProperty(value = "代理费费率类型")
    private String pRateType;

    @ApiModelProperty(value = "取值（买入，卖出）")
    private String rateValue;

    @ApiModelProperty(value = "汇率取值时间（09：00，10：00）")
    private String rateTime;

    @ApiModelProperty(value = "是否有最低收费")
    private BigDecimal isLowestPrice;

    @ApiModelProperty(value = "最低收费金额")
    private BigDecimal lowestPrice;

    @ApiModelProperty(value = "最低收费类型（按单，按日，按车)")
    private String lowestPriceType;

    @ApiModelProperty(value = "利息计算方式(按比例，按日)")
    private String interestType;

    @ApiModelProperty(value = "利息率")
    private BigDecimal interestFee;

    @ApiModelProperty(value = "免息天数")
    private Integer interestDayNo;

    @ApiModelProperty(value = "最低收费天数")
    private Integer interestDayLowest;

    @ApiModelProperty(value = "信用证服务费率")
    private BigDecimal lcFee;

    @ApiModelProperty(value = "逾期费率")
    private BigDecimal lateFee;

    @ApiModelProperty(value = "报关运费比例")
    private BigDecimal yFee;

    @ApiModelProperty(value = "报关保费比例")
    private BigDecimal bFee;

    @ApiModelProperty(value = "报关杂费比例")
    private BigDecimal oFee;

    @ApiModelProperty(value = "保证金比例")
    private BigDecimal earnestPer;

    @ApiModelProperty(value = "折扣率")
    private BigDecimal disCount;

    @ApiModelProperty(value = "是否扣减代理费（1、货款中扣减，2、退税款中扣减，3、暂不扣减）")
    private Integer proxyCharge;

    @ApiModelProperty(value = "出库收款方式")
    private String shippingFeeStyle;

    @ApiModelProperty(value = "发票处理方式（随货，先税后票，先开票后收税代）")
    private String invType;

    @ApiModelProperty(value = "信息服务费率")
    private BigDecimal msgServiceFee;

    @ApiModelProperty(value = "是否可合并最低收费结算")
    private Integer isMergeLowFee;

    @ApiModelProperty(value = "汇差处理方式(1.不开票，2.开6点服务费票,3.开13个点增值税票)")
    private Integer rateDiffType;

    @ApiModelProperty(value = "汇率取值银行（中行，招行）默认中行")
    private String rateBank;

    @ApiModelProperty(value = "固定加收费用")
    private BigDecimal addProxyFee;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "结算条款明细集合")
    private List<AddFeeListForm> addFeeListForms;

}
