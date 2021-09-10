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
 * 进项票明细表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ExportTaxInvoiceEntry对象", description="进项票明细表")
public class ExportTaxInvoiceEntry extends Model<ExportTaxInvoiceEntry> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "进项票主表id")
    private Integer exportInvId;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "订单明细id")
    private Integer orderEntryId;

    @ApiModelProperty(value = "型号")
    private String itemModel;

    @ApiModelProperty(value = "名称")
    private String itemName;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商税号")
    private String supplierTax;

    @ApiModelProperty(value = "开票数量")
    private BigDecimal invQty;

    @ApiModelProperty(value = "开票单价(含税)")
    private BigDecimal invPrice;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invMoney;

    @ApiModelProperty(value = "货款")
    private BigDecimal invGoods;

    @ApiModelProperty(value = "税款")
    private BigDecimal invTax;

    @ApiModelProperty(value = "开票单位")
    private String invUnit;

    @ApiModelProperty(value = "税票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invDtm;

    @ApiModelProperty(value = "ERP数据是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线进项票明细ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线发票代码")
    private String onlNo;

    @ApiModelProperty(value = "开票备注")
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
