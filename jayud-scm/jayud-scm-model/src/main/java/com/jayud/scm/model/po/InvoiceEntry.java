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
 * 结算明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="InvoiceEntry对象", description="结算明细表")
public class InvoiceEntry extends Model<InvoiceEntry> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "结算单主表ID")
    private Integer invoiceId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "订单明细ID")
    private Integer orderEntryId;

    @ApiModelProperty(value = "商品名称")
    private String itemName;

    @ApiModelProperty(value = "规格型号")
    private String itemModel;

    @ApiModelProperty(value = "单位")
    private String itemUnit;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "关联结算公式ID")
    private Integer feesId;

    @ApiModelProperty(value = "计算公式内容")
    private String formula;

    @ApiModelProperty(value = "应收日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime farDate;

    @ApiModelProperty(value = "费用名称")
    private String feeName;

    @ApiModelProperty(value = "金额")
    private BigDecimal arMoney;

    @ApiModelProperty(value = "费用别名")
    private String feeAlias;

    @ApiModelProperty(value = "首次完全核销日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fcDate;

    @ApiModelProperty(value = "发票生成时应收日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime farDateOld;

    @ApiModelProperty(value = "其它费用ID")
    private Integer otherCostId;

    @ApiModelProperty(value = "开票商品名称")
    private String itemNameInv;

    @ApiModelProperty(value = "开票规格型号")
    private String itemModelInv;

    @ApiModelProperty(value = "仅限于追踪付款")
    private Integer payId;

    @ApiModelProperty(value = "付款日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private String payDate;

    @ApiModelProperty(value = "付款汇率")
    private BigDecimal payRate;

    @ApiModelProperty(value = "是否用于开票")
    private Integer isBill;

    @ApiModelProperty(value = "是否用于实收核销")
    private Integer isAr;

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
    private LocalDateTime voidedByDtm;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
