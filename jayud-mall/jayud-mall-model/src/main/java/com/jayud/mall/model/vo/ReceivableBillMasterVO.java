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
public class ReceivableBillMasterVO {

    //主单
    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty(value = "客户ID(customer id)", position = 5)
    @JSONField(ordinal = 5)
    private Integer customerId;

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

    @ApiModelProperty(value = "客户名称(customer company)", position = 10)
    @JSONField(ordinal = 10)
    private String customerName;

    @ApiModelProperty(value = "账单金额(格式化)", position = 11)
    @JSONField(ordinal = 11)
    private String amountFormat;

    @ApiModelProperty(value = "账单日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //明细
    @ApiModelProperty(value = "应收账单明细list", position = 13)
    @JSONField(ordinal = 13)
    private List<ReceivableBillDetailVO> receivableBillDetailVOS;

    @ApiModelProperty(value = "订单编号", position = 14)
    @JSONField(ordinal = 14)
    private String orderNo;

    @ApiModelProperty(value = "法人主体", position = 15)
    @JSONField(ordinal = 15)
    private String legalEntity;

    @ApiModelProperty(value = "币种代码", position = 16)
    @JSONField(ordinal = 16)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 17)
    @JSONField(ordinal = 17)
    private String currencyName;


}
