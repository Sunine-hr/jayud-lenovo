package com.jayud.scm.model.po;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 销项票主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TaxInvoice对象", description="销项票主表")
public class TaxInvoice extends Model<TaxInvoice> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "销项票编号")
    private String taxNo;

    @ApiModelProperty(value = "发票代码")
    private String invcode;

    @ApiModelProperty(value = "增值税票号")
    private String invnumber;

    @ApiModelProperty(value = "开票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invdate;

    @ApiModelProperty(value = "客户ID")
    private Integer cId;

    @ApiModelProperty(value = "客户名称")
    private String cName;

    @ApiModelProperty(value = "客户税号")
    private String cTaxno;

    @ApiModelProperty(value = "客户银行和账号")
    private String cBank;

    @ApiModelProperty(value = "客户地址和电话")
    private String cAddress;

    @ApiModelProperty(value = "税率")
    private BigDecimal tax;

    @ApiModelProperty(value = "开票备注")
    private String taxRemark;

    @ApiModelProperty(value = "状态变更原因")
    private String changeReason;

    @ApiModelProperty(value = "配送ID")
    @TableField(fill = FieldFill.UPDATE)
    private Integer deliverId;

    @ApiModelProperty(value = "配送编号")
    @TableField(fill = FieldFill.UPDATE)
    private String deliverNo;

    @ApiModelProperty(value = "是否同步到开票系统")
    private Integer syncStatus;

    @ApiModelProperty(value = "ERP是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "快递公司")
    private String expressCompany;

    @ApiModelProperty(value = "快递单号")
    private String trackNumber;

    @ApiModelProperty(value = "用户备注")
    private String userRemark;

    @ApiModelProperty(value = "发票份数")
    private BigDecimal taxQty;

    @ApiModelProperty(value = "回传开票货款")
    private BigDecimal invTotalAmount;

    @ApiModelProperty(value = "回传开票税款")
    private BigDecimal invTotalTax;

    @ApiModelProperty(value = "回传开票价税合计")
    private BigDecimal invTotalMoney;

    @ApiModelProperty(value = "签收日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime signDate;

    @ApiModelProperty(value = "签收备注")
    private String signRemak;

    @ApiModelProperty(value = "签收状态（0:未签收，1已签收）")
    private Integer signState;

    @ApiModelProperty(value = "一审审核人")
    private String fMultiLevel0;

    @ApiModelProperty(value = "一审核 时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDateTime0;

    @ApiModelProperty(value = "终审人")
    private String fMultiLevel1;

    @ApiModelProperty(value = "终审时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDateTime1;

    @ApiModelProperty(value = "开票种类   0—专用发票，2—普通发票")
    private BigDecimal invoicekind;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步长")
    private Integer fStep;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "开票人")
    private String createName;

    @ApiModelProperty(value = "复核人")
    private String checkName;

    @ApiModelProperty(value = "收款人")
    private String recName;

    @ApiModelProperty(value = "是否有清单(如果明细为7条以上则为1，否则0)")
    private BigDecimal isList;

    @ApiModelProperty(value = "状态")
    private String stateflage;

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
