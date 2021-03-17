package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountPayableVO {

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "对账单编号", position = 1)
    @JSONField(ordinal = 2)
    private String dzdNo;

    @ApiModelProperty(value = "法人id(legal_person id)", position = 2)
    @JSONField(ordinal = 2)
    private Long legalPersonId;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "账期开始时间", position = 4)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 4, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDaysStart;

    @ApiModelProperty(value = "账期结束时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDaysEnd;

    @ApiModelProperty(value = "状态(0未付款 1已付款)", position = 6)
    @JSONField(ordinal = 6)
    private Integer status;

    @ApiModelProperty(value = "制单时间", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //金额(虚拟字段)
/*
    金额(虚拟字段)
    应付金额        payable_amount
    结算金额        balance_amount
    已付金额        paid_amount
    未付金额        unpaid_amount
 */

    @ApiModelProperty(value = "应付金额", position = 8)
    @JSONField(ordinal = 8)
    private BigDecimal payableAmount;

    @ApiModelProperty(value = "结算金额", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal balanceAmount;

    @ApiModelProperty(value = "已付金额", position = 10)
    @JSONField(ordinal = 10)
    private BigDecimal paidAmount;

    @ApiModelProperty(value = "未付金额", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal unpaidAmount;

    //法人主体
    @ApiModelProperty(value = "法人主体", position = 12)
    @JSONField(ordinal = 12)
    private String legalEntity;

    //供应商名称
    @ApiModelProperty(value = "供应商名称", position = 13)
    @JSONField(ordinal = 13)
    private String supplierName;

    //账期
    @ApiModelProperty(value = "账期", position = 14)
    @JSONField(ordinal = 14)
    private String paymentDays;

    //对账单关联的账单
    @ApiModelProperty(value = "对账单关联的账单(应付账单list)", position = 15)
    @JSONField(ordinal = 15)
    private List<PayBillMasterVO> payBillMasterVOS;


}
