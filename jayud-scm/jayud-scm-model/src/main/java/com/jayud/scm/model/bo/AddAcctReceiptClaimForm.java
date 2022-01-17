package com.jayud.scm.model.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 收款单表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-06
 */
@Data
public class AddAcctReceiptClaimForm {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "单据编号")
    private String fbillNo;

    @ApiModelProperty(value = "收款单类型(进口，出口)")
    private Integer modelType;

    @ApiModelProperty(value = "收款类型(正常,预收,押金)")
    private String payType;

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
    private String claimDate;

    @ApiModelProperty(value = "核销币别")
    private String currencyName;

    @ApiModelProperty(value = "折算汇率")
    private BigDecimal discountRate;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal money;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "关联水单ID")
    private Integer joinBillId;

    @ApiModelProperty(value = "备注")
    private String remark;

}
