package com.jayud.mall.model.po;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应付账单主单
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="PayBillMaster对象", description="应付账单主单")
public class PayBillMaster extends Model<PayBillMaster> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "账单编号(编码)")
    private String billCode;

    @ApiModelProperty(value = "订单ID(order_info id)")
    private Long orderId;

    @ApiModelProperty(value = "法人id(legal_person id)")
    private Long legalPersonId;

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Integer supplierId;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "账单状态(0未付款 1已付款)")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
