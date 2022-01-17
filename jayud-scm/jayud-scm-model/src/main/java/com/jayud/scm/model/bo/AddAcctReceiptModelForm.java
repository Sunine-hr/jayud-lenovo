package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 收款单表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class AddAcctReceiptModelForm {


    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "付款名称")
    private String payCompamyName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行帐号")
    private String bankNum;

    @ApiModelProperty(value = "原币币别")
    private String bankCurrency;

    @ApiModelProperty(value = "原币金额(银行收款金额)")
    private BigDecimal bankMoney;

//    @ApiModelProperty(value = "核销币别")
//    private String currencyName;
//
//    @ApiModelProperty(value = "核销金额")
//    private BigDecimal money;

    @ApiModelProperty(value = "手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "备注")
    private String remark;

}
