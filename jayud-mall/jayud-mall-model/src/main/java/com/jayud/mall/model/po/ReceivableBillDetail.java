package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 应收账单明细
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ReceivableBillDetail对象", description="应收账单明细")
public class ReceivableBillDetail extends Model<ReceivableBillDetail> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "账单主单id(receivable_bill_master id)", position = 2)
    @JSONField(ordinal = 2)
    private Long billMasterId;

    @ApiModelProperty(value = "订单应收费用明细id(order_cope_receivable id)", position = 3)
    @JSONField(ordinal = 3)
    private Long orderReceivableId;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)", position = 4)
    @JSONField(ordinal = 4)
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)", position = 5)
    @JSONField(ordinal = 5)
    private String costName;

    @ApiModelProperty(value = "金额", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 7)
    @JSONField(ordinal = 7)
    private Integer cid;

    @ApiModelProperty(value = "描述", position = 8)
    @JSONField(ordinal = 8)
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
