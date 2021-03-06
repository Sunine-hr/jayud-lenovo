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
 * 付款单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AcctPay对象", description="付款单主表")
public class AcctPay extends Model<AcctPay> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "付款编号")
    private String payNo;

    @ApiModelProperty(value = "付款类型(进口，出口)")
    private Integer modelType;

    @ApiModelProperty(value = "付款方式")
    private String payType;

    @ApiModelProperty(value = "申请付款日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "预计到货日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arrivedDate;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "付款汇率")
    private BigDecimal payRate;

    @ApiModelProperty(value = "成本汇率")
    private BigDecimal acctRate;

    @ApiModelProperty(value = "申请付款汇率")
    private BigDecimal appPayRate;

    @ApiModelProperty(value = "是否垫付0,不垫资，1.垫资")
    private Integer isCredit;

    @ApiModelProperty(value = "是否由我司承担")
    private Integer isMyFee;

    @ApiModelProperty(value = "是否 为预付款")
    private Integer isYuFee;

    @ApiModelProperty(value = "是否为定金")
    private Integer isDingFee;

    @ApiModelProperty(value = "锁定标志（锁定不允许付款）")
    private Integer isLock;

    @ApiModelProperty(value = "结算方案ID预付需选择")
    private Integer arfeeId;

    @ApiModelProperty(value = "应付原币币别")
    private String currencyName;

    @ApiModelProperty(value = "应付原币金额")
    private BigDecimal apMoney;

    @ApiModelProperty(value = "出口扣代理费")
    private BigDecimal proxyMoney;

    @ApiModelProperty(value = "实付币别")
    private String payCurrencyName;

    @ApiModelProperty(value = "实付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(value = "收款银行ID")
    private Integer acctBankId;

    @ApiModelProperty(value = "收款银行编号")
    private String acctBankNo;

    @ApiModelProperty(value = "收款银行")
    private String acctBank;

    @ApiModelProperty(value = "收款单位名称")
    private String acctBankName;

    @ApiModelProperty(value = "收款银行帐号")
    private String acctCode;

    @ApiModelProperty(value = "银行地址")
    private String acctAddr;

    @ApiModelProperty(value = "swich_code")
    private String swichCode;

    @ApiModelProperty(value = "区域")
    private String acctArea;

    @ApiModelProperty(value = "付款操作人")
    private String payUser;

    @ApiModelProperty(value = "付款操作时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "付款公司ID")
    private Integer payCompanyId;

    @ApiModelProperty(value = "付款公司名字")
    private String payCompanyName;

    @ApiModelProperty(value = "付款银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "付款银行")
    private String bankName;

    @ApiModelProperty(value = "银行流水号")
    private String bankNumber;

    @ApiModelProperty(value = "银行手续费币别")
    private String shouFeeCurrency;

    @ApiModelProperty(value = "手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "付款手续费承担方(公司，客户，供应商)")
    private String shouFeeBearer;

    @ApiModelProperty(value = "确认付款日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualPayDate;

    @ApiModelProperty(value = "实际确认付款人")
    private String actualPayName;

    @ApiModelProperty(value = "是否付款（0：未付款，1：付款）")
    private Integer isPay;

    @ApiModelProperty(value = "财务付款备注")
    private String payRemark;

    @ApiModelProperty(value = "用途")
    private String uses;

    @ApiModelProperty(value = "支票ID")
    private Integer chequeId;

    @ApiModelProperty(value = "支票号")
    private String chequeNo;

    @ApiModelProperty(value = "支票状态 0未取/1已取")
    private Integer chequeState;

    @ApiModelProperty(value = "支票交接签收日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime chequeDate;

    @ApiModelProperty(value = "期票开票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginData;

    @ApiModelProperty(value = "期票到期日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "出口预收款ID")
    private Integer payToMeId;

    @ApiModelProperty(value = "收款金额")
    private BigDecimal receiptMoney;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步长")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "ERP数据是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线应付ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线应付编号")
    private String onlNo;

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
