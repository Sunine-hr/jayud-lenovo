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
 * 核销列表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="VerificationReocrds对象", description="核销列表")
public class VerificationReocrds extends Model<VerificationReocrds> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "核销单号")
    private String fBillNo;

    @ApiModelProperty(value = "核销日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDate;

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

    @ApiModelProperty(value = "核销金额")
    private BigDecimal cMoney;

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

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步骤")
    private Integer fStep;

    @ApiModelProperty(value = "是否已经同步（0未同步，1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String voidedByName;

    @ApiModelProperty(value = "删除时间")
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
