package com.jayud.scm.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应付款表（付款单明细表）
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="AcctPayEntry对象", description="应付款表（付款单明细表）")
public class AcctPayEntry extends Model<AcctPayEntry> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "应付款编号")
    private String fBillNo;

    @ApiModelProperty(value = "应付款日期")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "金额")
    private BigDecimal apMoney;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "付款供应商ID")
    private Integer paySupplierId;

    @ApiModelProperty(value = "付款供应商")
    private String paySupplierName;

    @ApiModelProperty(value = "收款银行ID")
    private Integer acctBankId;

    @ApiModelProperty(value = "收款银行编号")
    private String acctBankNo;

    @ApiModelProperty(value = "收款银行")
    private String acctBank;

    @ApiModelProperty(value = "收款银行帐号")
    private String acctCode;

    @ApiModelProperty(value = "银行地址")
    private String acctAddr;

    @ApiModelProperty(value = "swich_code")
    private String swichCode;

    @ApiModelProperty(value = "收款银行区域（境内，香港，境外）")
    private Integer acctArea;

    @ApiModelProperty(value = "付款类型(进口，出口)")
    private Integer modelType;

    @ApiModelProperty(value = "付款方式(TT/COD)")
    private String payType;

    @ApiModelProperty(value = "TT类型(货前货后)")
    private String ttType;

    @ApiModelProperty(value = "税票ID")
    private Integer taxInvId;

    @ApiModelProperty(value = "减扣代理费")
    private BigDecimal proxyMoney;

    @ApiModelProperty(value = "付款ID")
    private Integer payId;

    @ApiModelProperty(value = "出口付款类型（税款，货款）--(预付款、尾款)")
    private String payModelType;

    @ApiModelProperty(value = "ERP数据是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线应付ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线应付编号")
    private String onlNo;

    @ApiModelProperty(value = "采购合同ID对应booking_contract中主键")
    private Integer orderContractId;

    @ApiModelProperty(value = "采购合同No对应booking_contract中合同编号")
    private String orderContractNo;

    @ApiModelProperty(value = "出口核销表ID")
    private Integer exportVerificationId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人ID")
    private Integer crtBy;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人ID")
    private Integer mdyBy;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime mdyByDtm;

    @ApiModelProperty(value = "删除标记")
    private Integer voided;

    @ApiModelProperty(value = "删除人ID")
    private Integer voidedBy;

    @ApiModelProperty(value = "删除人名称")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
