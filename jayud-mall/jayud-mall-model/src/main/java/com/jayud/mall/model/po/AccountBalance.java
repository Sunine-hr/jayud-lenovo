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
 * 账户余额表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AccountBalance对象", description="账户余额表")
public class AccountBalance extends Model<AccountBalance> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "客户ID(customer id)", position = 2)
    @JSONField(ordinal = 2)
    private Long customerId;

    @ApiModelProperty(value = "币种(currency_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Long cid;

    @ApiModelProperty(value = "金额", position = 4)
    @JSONField(ordinal = 4)
    private BigDecimal amount;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
