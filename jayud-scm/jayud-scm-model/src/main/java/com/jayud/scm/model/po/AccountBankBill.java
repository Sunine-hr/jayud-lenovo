package com.jayud.scm.model.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 水单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AccountBankBill对象", description="水单主表")
public class AccountBankBill extends Model<AccountBankBill> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务类型(0：进口，1：出口)")
    private Integer modelType;

    @ApiModelProperty(value = "水单编号")
    private String bankBillNo;

    @ApiModelProperty(value = "付款编号")
    private String payNo;

    @ApiModelProperty(value = "水单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bankBillDate;

    @ApiModelProperty(value = "收到水单日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String bankBillTime;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "业务员ID")
    private Integer fsalerId;

    @ApiModelProperty(value = "业务员")
    private String fsalerName;

    @ApiModelProperty(value = "商务ID")
    private Integer followerId;

    @ApiModelProperty(value = "商务跟单")
    private String followerName;

    @ApiModelProperty(value = "水单币别")
    private String currencyName;

    @ApiModelProperty(value = "水单金额")
    private BigDecimal billMoney;

    @ApiModelProperty(value = "实际到帐金额")
    private BigDecimal billArMoney;

    @ApiModelProperty(value = "手续费承担方")
    private String shouFeeCdf;

    @ApiModelProperty(value = "到账手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "到账日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accountDate;

    @ApiModelProperty(value = "到账时间")
    private String accountTime;

    @ApiModelProperty(value = "银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行帐号")
    private String bankNum;

    @ApiModelProperty(value = "水单类别（水单，预收款）")
    private String bankBillType;

    @ApiModelProperty(value = "实际付款公司ID")
    private Integer payCustomerId;

    @ApiModelProperty(value = "实际付款公司名称")
    private String payCustomerName;

    @ApiModelProperty(value = "核销金额")
    @TableField(fill = FieldFill.UPDATE)
    private BigDecimal verificationMoney;

    @ApiModelProperty(value = "核销金额是否取到账金额(0取1不取)")
    private Integer isVerification;

    @ApiModelProperty(value = "核销币别")
    private String verificationCurrency;

    @ApiModelProperty(value = "核销汇率")
    private BigDecimal verificationRate;

    @ApiModelProperty(value = "折算汇率")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步长")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

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
