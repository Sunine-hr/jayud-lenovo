package com.jayud.scm.model.bo;

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
 * 应付款表（付款单明细表）
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
public class AddAcctPayEntryForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "应付款编号")
    @JsonProperty(value = "fBillNo")
    private String fBillNo;

    @ApiModelProperty(value = "应付款日期")
    @JsonProperty(value = "fDate")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "金额")
    @JsonProperty(value = "apMoney")
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
    @JsonProperty(value = "swichCode")
    private String swichCode;

    @ApiModelProperty(value = "收款银行区域（境内，香港，境外）")
    private Integer acctArea;

    @ApiModelProperty(value = "付款类型(进口，出口)")
    private Integer modelType;

    @ApiModelProperty(value = "付款方式(TT/COD)")
    private String payType;

    @ApiModelProperty(value = "TT类型(货前货后)")
    @JsonProperty(value = "ttType")
    private String ttType;

    @ApiModelProperty(value = "税票ID")
    @JsonProperty(value = "taxInvId")
    private Integer taxInvId;

    @ApiModelProperty(value = "减扣代理费")
    private BigDecimal proxyMoney;

    @ApiModelProperty(value = "付款ID")
    private Integer payId;

    @ApiModelProperty(value = "出口付款类型（税款，货款）--(预付款、尾款)")
    private String payModelType;

    @ApiModelProperty(value = "采购合同ID对应booking_contract中主键")
    private Integer orderContractId;

    @ApiModelProperty(value = "采购合同No对应booking_contract中合同编号")
    private String orderContractNo;

    @ApiModelProperty(value = "出口核销表ID")
    private Integer exportVerificationId;

    @ApiModelProperty(value = "备注")
    private String remark;
}
