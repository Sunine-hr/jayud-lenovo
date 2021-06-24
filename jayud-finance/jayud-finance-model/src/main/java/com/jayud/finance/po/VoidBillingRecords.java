package com.jayud.finance.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 作废账单表
 * </p>
 *
 * @author chuanmei
 * @since 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "VoidBillingRecords对象", description = "作废账单表")
public class VoidBillingRecords extends Model<VoidBillingRecords> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主订单编号")
    private String mainOrderNo;

    @ApiModelProperty(value = "子订单号")
    private String orderNo;

    @ApiModelProperty(value = "账单ID")
    private Long billId;

    @ApiModelProperty(value = "核算期 XXXX-XX")
    private String accountTerm;

    @ApiModelProperty(value = "结算币种")
    private String settlementCurrency;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "生成账单人")
    private String makeUser;

    @ApiModelProperty(value = "生成账单时间")
    private LocalDateTime makeTime;

    @ApiModelProperty(value = "审核人")
    private String auditUser;

    @ApiModelProperty(value = "审核时间")
    private LocalDateTime auditTime;

    @ApiModelProperty(value = "开票状态 0-未申请  1-待审核 2-审核通过 3-审核驳回")
    private String applyStatus;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "审核状态")
    private String auditStatus;

    @ApiModelProperty(value = "建单日期")
    private LocalDate createdOrderTime;

    @ApiModelProperty(value = "应收费用ID")
    private Long costId;

    @ApiModelProperty(value = "费用类型/类别/名称维度的本币金额")
    private BigDecimal localAmount;

    @ApiModelProperty(value = "费用维度的备注")
    private String remarks;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdTime;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "费用项")
    private String costInfoName;

    @ApiModelProperty(value = "结算金额")
    private BigDecimal money;

    @ApiModelProperty(value = "本币汇率")
    private BigDecimal localMoneyRate;

    @ApiModelProperty(value = "费用类型(0-应收, 1-应付)")
    private String costType;

    @ApiModelProperty(value = "当前币种")
    private String currentCurrencyCode;

    @ApiModelProperty(value = "结算汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(value = "是否自定汇率")
    private Boolean isCustomExchangeRate;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty(value = "法人主体姓名")
    private String legalName;

    @ApiModelProperty(value = "结算单位/供应商名称")
    private String customerName;

    @ApiModelProperty(value = "应收/应付金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "如果是已子订单维度出账的,则记录具体的子订单类型")
    private String subType;

    @ApiModelProperty(value = "是否汇总到主订单")
    private Boolean isSumToMain;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
