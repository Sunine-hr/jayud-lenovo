package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PayBillMasterVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "账单编号(编码)", position = 2)
    @JSONField(ordinal = 2)
    private String billCode;

    @ApiModelProperty(value = "订单ID(order_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Long orderId;

    @ApiModelProperty(value = "法人id(legal_person id)", position = 4)
    @JSONField(ordinal = 4)
    private Long legalPersonId;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 5)
    @JSONField(ordinal = 5)
    private Integer supplierId;

    @ApiModelProperty(value = "金额", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 7)
    @JSONField(ordinal = 7)
    private Integer cid;

    @ApiModelProperty(value = "描述", position = 8)
    @JSONField(ordinal = 8)
    private String remarks;

    @ApiModelProperty(value = "账单状态(0未付款 1已付款)", position = 9)
    @JSONField(ordinal = 9)
    private Integer status;

    @ApiModelProperty(value = "供应商名称(supplier_info company_name)", position = 10)
    @JSONField(ordinal = 10)
    private String supplierName;

    @ApiModelProperty(value = "账单金额(格式化)-明细合计", position = 11)
    @JSONField(ordinal = 11)
    private String amountFormat;

    @ApiModelProperty(value = "账单日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单编码", position = 13)
    @JSONField(ordinal = 13)
    private String orderNo;

    @ApiModelProperty(value = "法人主体", position = 14)
    @JSONField(ordinal = 14)
    private String legalEntity;

    @ApiModelProperty(value = "币种代码", position = 15)
    @JSONField(ordinal = 15)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 16)
    @JSONField(ordinal = 16)
    private String currencyName;

    @ApiModelProperty(value = "应付账单明细list", position = 17)
    @JSONField(ordinal = 17)
    private List<PayBillDetailVO> payBillDetailVOS;

    //新增字段
    @ApiModelProperty(value = "付款人(system_user id)", position = 18)
    @JSONField(ordinal = 18)
    private Long payerId;

    @ApiModelProperty(value = "付款日期", position = 19)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 19, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @ApiModelProperty(value = "交易凭证(url)", position = 20)
    @JSONField(ordinal = 20)
    private String voucherUrl;

    @ApiModelProperty(value = "应付对账单id", position = 21)
    @JSONField(ordinal = 21)
    private Long accountPayableId;

    //账单金额
    @ApiModelProperty(value = "账单金额", position = 22)
    @JSONField(ordinal = 22)
    private List<String> billAmount;

    //结算金额
    @ApiModelProperty(value = "结算金额(数值 + 单位)", position = 23)
    @JSONField(ordinal = 23)
    private List<String> balanceAmount;

    @ApiModelProperty(value = "结算金额(数值)", position = 24)
    @JSONField(ordinal = 24)
    private BigDecimal balanceAmountCNY;

    @ApiModelProperty(value = "结算金额(币种id)", position = 25)
    @JSONField(ordinal = 25)
    private Integer balanceAmountCid;

    @ApiModelProperty(value = "交易凭证(url)文件上传", position = 26)
    @JSONField(ordinal = 26)
    private List<TemplateUrlVO> voucherUrls;

}
