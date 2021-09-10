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
 * 收款单表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AcctReceipt对象", description="收款单表")
public class AcctReceipt extends Model<AcctReceipt> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "单据编号")
    private String fbillNo;

    @ApiModelProperty(value = "收款单类型(进口，出口)")
    private Integer modelType;

    @ApiModelProperty(value = "收款类型(正常,预收,押金)")
    private String payType;

    @ApiModelProperty(value = "预收款是否释放额度1为已释放")
    private Integer creditFlag;

    @ApiModelProperty(value = "付款日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "到帐日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billDate;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "付款方ID")
    private Integer payCompamyId;

    @ApiModelProperty(value = "付款名称")
    private String payCompamyName;

    @ApiModelProperty(value = "银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行帐号")
    private String bankNum;

    @ApiModelProperty(value = "银行流水号")
    private String bankFlowNo;

    @ApiModelProperty(value = "银行备注")
    private String receiptRemark;

    @ApiModelProperty(value = "原币币别")
    private String bankCurrency;

    @ApiModelProperty(value = "原币金额(银行收款金额)")
    private BigDecimal bankMoney;

    @ApiModelProperty(value = "出口结汇汇率")
    private BigDecimal accRate;

    @ApiModelProperty(value = "认领状态：0未认领，1已认领")
    private Integer isClaim;

    @ApiModelProperty(value = "认领人ID")
    private Integer claimId;

    @ApiModelProperty(value = "认领人姓名")
    private String claimName;

    @ApiModelProperty(value = "认领时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime claimDate;

    @ApiModelProperty(value = "核销币别")
    private String currencyName;

    @ApiModelProperty(value = "折算汇率")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal money;

    @ApiModelProperty(value = "锁定金额")
    private BigDecimal lockMoney;

    @ApiModelProperty(value = "手续费承担方")
    private String shouFeeCdf;

    @ApiModelProperty(value = "手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "关联水单ID")
    private Integer joinBillId;

    @ApiModelProperty(value = "押金是否已退")
    private Integer backForegift;

    @ApiModelProperty(value = "ERP是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线应付ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线应付编号")
    private String onlNo;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

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
