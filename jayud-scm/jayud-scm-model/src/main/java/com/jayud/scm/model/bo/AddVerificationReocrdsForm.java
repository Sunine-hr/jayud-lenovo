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
 * 核销列表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-08
 */
@Data
public class AddVerificationReocrdsForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "核销单号")
    @JsonProperty(value = "fBillNo")
    private String fBillNo;

    @ApiModelProperty(value = "核销日期")
    @JsonProperty(value = "fDate")
    private String fDate;

    @ApiModelProperty(value = "收款单ID")
    private Integer payId;

    @ApiModelProperty(value = "应收款ID")
    private Integer invoiceId;

    @ApiModelProperty(value = "应收款明细ID")
    private Integer invoiceEntryId;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "委托单id")
    private Integer orderId;

    @ApiModelProperty(value = "委托单号")
    private String orderNO;

    @ApiModelProperty(value = "核销金额")
    @JsonProperty(value = "nMoney")
    private BigDecimal nMoney;

    @ApiModelProperty(value = "分摊的银行手续费")
    private BigDecimal shouFee;

    @ApiModelProperty(value = "手续费承担方")
    private String shouFeeCdf;

    @ApiModelProperty(value = "核销金额对应扣减代理费")
    private BigDecimal proxyFee;

    @ApiModelProperty(value = "出口应付ID")
    private Integer payEntryId;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

}
