package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 水单主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class AddAccountBankBillForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务类型(0：进口，1：出口)")
    private Integer modelType;

    @ApiModelProperty(value = "水单编号")
    private String bankBillNo;

    @ApiModelProperty(value = "水单日期")
    @NotNull(message = "水单日期不为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String bankBillDate;

    @ApiModelProperty(value = "收到水单日期")
    @NotNull(message = "收到水单日期不为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String bankBillTime;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    @NotNull(message = "客户名称不为空")
    private String customerName;

    @ApiModelProperty(value = "业务员ID")
    private Integer fsalerId;

    @ApiModelProperty(value = "业务员")
    @NotNull(message = "业务员不为空")
    private String fsalerName;

    @ApiModelProperty(value = "商务ID")
    private Integer followerId;

    @ApiModelProperty(value = "商务跟单")
    @NotNull(message = "商务跟单不为空")
    private String followerName;

    @ApiModelProperty(value = "水单币别")
    @NotNull(message = "水单币别不为空")
    private String currencyName;

    @ApiModelProperty(value = "水单金额")
    @NotNull(message = "水单金额不为空")
    private BigDecimal billMoney;

    @ApiModelProperty(value = "实际到帐金额")
    private BigDecimal billArMoney;

    @ApiModelProperty(value = "手续费承担方")
    @NotNull(message = "手续费承担方不为空")
    private String shouFeeCdf;

    @ApiModelProperty(value = "到账手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "到账日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String accountDate;

    @ApiModelProperty(value = "到账时间")
    private String accountTime;

    @ApiModelProperty(value = "银行ID")
    @NotNull(message = "银行ID不为空")
    private Integer bankId;

    @ApiModelProperty(value = "银行名称")
    @NotNull(message = "银行名称不为空")
    private String bankName;

    @ApiModelProperty(value = "银行帐号")
    @NotNull(message = "银行帐号不为空")
    private String bankNum;

    @ApiModelProperty(value = "水单类别（水单，预收款）")
    @NotNull(message = "水单类别不为空")
    private String bankBillType;

    @ApiModelProperty(value = "实际付款公司ID")
    private Integer payCustomerId;

    @ApiModelProperty(value = "实际付款公司名称")
    private String payCustomerName;

    @ApiModelProperty(value = "核销金额")
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

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

}
