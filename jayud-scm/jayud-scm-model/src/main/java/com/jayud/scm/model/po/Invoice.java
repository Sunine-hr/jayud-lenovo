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
 * 结算单（应收款）主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Invoice对象", description="结算单（应收款）主表")
public class Invoice extends Model<Invoice> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发票号码")
    private String fBillNo;

    @ApiModelProperty(value = "日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "客户ID")
    private Integer customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "币别")
    private String currencyName;

    @ApiModelProperty(value = "业务类型，进口，出口，分销，香港交货")
    private Integer modelType;

    @ApiModelProperty(value = "代理方式")
    private String deputyStyle;

    @ApiModelProperty(value = "付款方式")
    private String payStyle;

    @ApiModelProperty(value = "收款方式")
    private String recStyle;

    @ApiModelProperty(value = "汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "是否已发邮件")
    private Integer isSendEmail;

    @ApiModelProperty(value = "发送邮件时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "0 ：进口13点税票， 1：进口6个点的税票，2：应收外汇，3：出口应收代理费，4：转口应收，5：应收账期费，6：国内贸易应收款，7：汇差，8：其他费用, 9：增值服务，10：应收账期费，11：应收逾期费")
    private Integer landTaxFlag;

    @ApiModelProperty(value = "是否打印")
    private Integer isPrint;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "审核级别")
    private Integer fLevel;

    @ApiModelProperty(value = "审核步长")
    private Integer fStep;

    @ApiModelProperty(value = "开票备注")
    private String taxRemark;

    @ApiModelProperty(value = "ERP是否同步在线（0未同步1已同步）")
    private Integer isSync;

    @ApiModelProperty(value = "在线ID")
    private Integer onlId;

    @ApiModelProperty(value = "在线编号")
    private String onlNo;

    @ApiModelProperty(value = "付款单扣除")
    private Integer payId;

    @ApiModelProperty(value = "开票客户ID")
    private Integer invCustomerId;

    @ApiModelProperty(value = "开票付款客户名称(用于做购销协议及付款)")
    private String invCustomerName;

    @ApiModelProperty(value = "是否报关 0表为未报关，否则已报关")
    private Integer isHgbill;

    @ApiModelProperty(value = "收款银行ID")
    private Integer bankId;

    @ApiModelProperty(value = "是否已做负数发票或为负数发票")
    private Integer isRed;

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
