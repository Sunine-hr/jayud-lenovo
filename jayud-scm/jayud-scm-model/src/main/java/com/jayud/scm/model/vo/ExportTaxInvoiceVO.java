package com.jayud.scm.model.vo;

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
 * 进项票主表
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@Data
public class ExportTaxInvoiceVO {

    @ApiModelProperty(value = "自动ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "单据编号")
    private String fBillNo;

    @ApiModelProperty(value = "税票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fDate;

    @ApiModelProperty(value = "付款类型(国内，国外)")
    private Integer modelType;

    @ApiModelProperty(value = "供应商ID")
    private Integer supplierId;

    @ApiModelProperty(value = "供应商")
    private String supplierName;

    @ApiModelProperty(value = "订单ID")
    private Integer orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "税票类型")
    private String invTaxType;

    @ApiModelProperty(value = "是否抵扣")
    private BigDecimal isOffset;

    @ApiModelProperty(value = "认证时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmDtm;

    @ApiModelProperty(value = "收票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime getFDate;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "发票采购金额(货款金额)")
    private BigDecimal invMoney;

    @ApiModelProperty(value = "发票税金（税款金额）")
    private BigDecimal invTaxMoney;

    @ApiModelProperty(value = "发票价税合计")
    private BigDecimal invTotalMoney;

    @ApiModelProperty(value = "退税提交日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitDate;

    @ApiModelProperty(value = "可退金额 根据实际开票计算得出")
    private BigDecimal invBackTaxMoney;

    @ApiModelProperty(value = "状态")
    private Integer stateFlag;

    @ApiModelProperty(value = "审核状态")
    private String checkStateFlag;

    @ApiModelProperty(value = "到帐日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime arriveTime;

    @ApiModelProperty(value = "实退税到账金额")
    private BigDecimal factBackTaxMoney;

    @ApiModelProperty(value = "到帐银行id")
    private Integer bankId;

    @ApiModelProperty(value = "到帐银行名称")
    private String bankName;

    @ApiModelProperty(value = "函调状态（函调中，函调完成）")
    private String letterFlag;

    @ApiModelProperty(value = "发函时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime letterDate;

    @ApiModelProperty(value = "回函时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime backletterDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人名称")
    private String crtByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime crtByDtm;

    @ApiModelProperty(value = "最后修改人名称")
    private String mdyByName;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime mdyByDtm;

}
