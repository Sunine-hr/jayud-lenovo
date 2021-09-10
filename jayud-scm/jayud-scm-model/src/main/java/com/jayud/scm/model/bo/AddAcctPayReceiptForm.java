package com.jayud.scm.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 付款单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
public class AddAcctPayReceiptForm {

//    @ApiModelProperty(value = "自动ID")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Integer id;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "结算方案ID预付需选择")
    private Integer arfeeId;

    @ApiModelProperty(value = "应付原币币别")
    private String currencyName;

    @ApiModelProperty(value = "应付原币金额")
    private BigDecimal apMoney;

    @ApiModelProperty(value = "出口扣代理费")
    private BigDecimal proxyMoney;

//    @ApiModelProperty(value = "实付币别")
//    private String payCurrencyName;
//
//    @ApiModelProperty(value = "实付金额")
//    private BigDecimal payMoney;

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

    @ApiModelProperty(value = "区域")
    private String acctArea;

    @ApiModelProperty(value = "付款银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "付款银行")
    private String bankName;

    @ApiModelProperty(value = "银行流水号")
    private String bankNumber;

    @ApiModelProperty(value = "出口结汇汇率")
    private BigDecimal accRate;


}
