package com.jayud.scm.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 结算单（应收款）主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
public class InvoiceDetailVO {

    @ApiModelProperty(value = "应收款明细ID")
    private Integer invoiceEntryId;

    @ApiModelProperty(value = "发票号码")
    @JsonProperty(value = "fBillNo")
    private String fBillNo;

    @ApiModelProperty(value = "日期")
    @JsonProperty(value = "fDate")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "业务类型，进口，出口，分销，香港交货")
    private Integer modelType;

    @ApiModelProperty(value = "代理方式")
    private String deputyStyle;

    @ApiModelProperty(value = "付款方式")
    private String payStyle;

    @ApiModelProperty(value = "收款方式")
    private String recStyle;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "是否已发邮件")
    private Integer isSendEmail;

    @ApiModelProperty(value = "发送邮件时间")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "0 ：进口13点税票， 1：进口6个点的税票，2：应收外汇，3：出口应收代理费，4：转口应收，5：应收账期费，6：国内贸易应收款，7：汇差，8：其他费用, 9：增值服务，10：应收账期费，11：应收逾期费")
    private Integer landTaxFlag;

    @ApiModelProperty(value = "是否打印")
    private Integer isPrint;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "开票备注")
    private String taxRemark;

    @ApiModelProperty(value = "付款单扣除")
    private Integer payId;

    @ApiModelProperty(value = "开票客户ID")
    private Integer invCustomerId;

    @ApiModelProperty(value = "开票付款客户名称(用于做购销协议及付款)")
    private String invCustomerName;

    @ApiModelProperty(value = "是否报关 0表为未报关，否则已报关")
    private Integer isHgbill;

    @ApiModelProperty(value = "收款银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "是否已做负数发票或为负数发票")
    private Integer isRed;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "自动ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer bid;

    @ApiModelProperty(value = "结算单主表ID")
    private Integer invoiceId;

    @ApiModelProperty(value = "日期")
    private LocalDateTime bfDate;

    @ApiModelProperty(value = "订单明细ID")
    private Integer orderEntryId;

    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "规格型号")
    private String itemModel;

    @ApiModelProperty(value = "单位")
    private String itemUnit;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "汇率")
    private BigDecimal brate;

    @ApiModelProperty(value = "关联结算公式ID")
    private Integer feesId;

    @ApiModelProperty(value = "计算公式内容")
    private String formula;

    @ApiModelProperty(value = "应收日期")
    private LocalDateTime farDate;

    @ApiModelProperty(value = "费用名称")
    private String feeName;

    @ApiModelProperty(value = "金额")
    @JsonProperty(value = "arMoney")
    private BigDecimal arMoney;

    @ApiModelProperty(value = "费用别名")
    private String feeAlias;

    @ApiModelProperty(value = "首次完全核销日期")
    @JsonProperty(value = "fcDate")
    private LocalDateTime fcDate;

    @ApiModelProperty(value = "发票生成时应收日期")
    private LocalDateTime farDateOld;

    @ApiModelProperty(value = "其它费用ID")
    private Integer otherCostId;

    @ApiModelProperty(value = "开票商品名称")
    private String itemNameInv;

    @ApiModelProperty(value = "开票规格型号")
    private String itemModelInv;

    @ApiModelProperty(value = "仅限于追踪付款")
    @JsonProperty(value = "bpayId")
    private Integer bpayId;

    @ApiModelProperty(value = "付款日期")
    private String payDate;

    @ApiModelProperty(value = "付款汇率")
    private BigDecimal payRate;

    @ApiModelProperty(value = "是否用于开票")
    private Integer isBill;

    @ApiModelProperty(value = "是否用于实收核销")
    private Integer isAr;

}
