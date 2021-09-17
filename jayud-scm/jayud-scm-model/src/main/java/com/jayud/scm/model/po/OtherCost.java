package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 其他费用记录表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="OtherCost对象", description="其他费用记录表")
public class OtherCost extends Model<OtherCost> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "费用日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "费用名称ID")
    private Integer itemId;

    @ApiModelProperty(value = "费用名称")
    private String itemName;

    @ApiModelProperty(value = "业务类型(选择0进口、1出口)")
    private Integer modelType;

    @ApiModelProperty(value = "费用发生单ID")
    private Integer orderId;

    @ApiModelProperty(value = "费用发生单号")
    private String orderNo;

    @ApiModelProperty(value = "结算单ID")
    private Integer settlementId;

    @ApiModelProperty(value = "结算单号")
    private String settlementNo;

    @ApiModelProperty(value = "结算单订单明细id")
    private Integer orderEntryId;

    @ApiModelProperty(value = "业务实体(默认：该公司)")
    private String businessName;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "费用金额（协议金额）")
    private BigDecimal cost;

    @ApiModelProperty(value = "实际付款金额（发生额）")
    private BigDecimal realCost;

    @ApiModelProperty(value = "汇率")
    private BigDecimal costRate;

    @ApiModelProperty(value = "付款方式(月结、票结)")
    private String payType;

    @ApiModelProperty(value = "结算单ID")
    private Integer invoiceId;

    @ApiModelProperty(value = "结算单码")
    private String invoiceNo;

    @ApiModelProperty(value = "承担方（我司承担，客户承担，供应商承担）")
    private String bearParty;

    @ApiModelProperty(value = "是否开票（0未开票，1已开票）")
    private Integer isBilling;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "一审人")
    private String fMultiLevei0;

    @ApiModelProperty(value = "一审时间")
    private LocalDateTime dDateTime0;

    @ApiModelProperty(value = "二审人")
    private String fMultiLevel1;

    @ApiModelProperty(value = "二审时间")
    private LocalDateTime fDateTime1;

    @ApiModelProperty(value = "减免状态(0未申请，1已申请，2减免审核通过，3减免审核不通过)")
    private Integer deductionStatus;

    @ApiModelProperty(value = "减免金额")
    private BigDecimal deductionMoney;

    @ApiModelProperty(value = "申请理由")
    private String applicationReason;

    @ApiModelProperty(value = "审核日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;

    @ApiModelProperty(value = "减免审核备注")
    private String reviewRemark;

    @ApiModelProperty(value = "付款id")
    private Integer payId;

    @ApiModelProperty(value = "水单id")
    private Integer accountBillId;

    @ApiModelProperty(value = "汇差扣除ID")
    private Integer acctRateId;

    @ApiModelProperty(value = "服务费ID")
    private Integer differentId;

    @ApiModelProperty(value = "服务费编号")
    private String differentNo;

    @ApiModelProperty(value = "是否核销")
    private Integer isVerification;

    @ApiModelProperty(value = "核销备注")
    private String verificationRemark;

    @ApiModelProperty(value = "核销人")
    private String verificationName;

    @ApiModelProperty(value = "核销 时间")
    private LocalDateTime verificationDtm;

    @ApiModelProperty(value = "状态(0待审核，1已审核，2待确认，3可付款，4已付款)")
    private Integer stateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
