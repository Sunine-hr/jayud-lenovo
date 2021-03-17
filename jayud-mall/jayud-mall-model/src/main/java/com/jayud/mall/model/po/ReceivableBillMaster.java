package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 应收账单主单
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReceivableBillMaster对象", description="应收账单主单")
public class ReceivableBillMaster extends Model<ReceivableBillMaster> {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "账单日期", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //新增字段
    @ApiModelProperty(value = "核销人(system_user id)", position = 11)
    @JSONField(ordinal = 11)
    private Long verificationUserId;

    @ApiModelProperty(value = "核销日期", position = 12)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 12, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verificationTime;

    @ApiModelProperty(value = "应收对账单id(account_receivable id)", position = 13)
    @JSONField(ordinal = 13)
    private Long accountReceivableId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
