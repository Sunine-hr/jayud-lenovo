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

/**
 * <p>
 * 提单对应货柜信息和客户关联表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanCounterCustomerRelation对象", description="提单对应货柜信息和客户关联表")
public class OceanCounterCustomerRelation extends Model<OceanCounterCustomerRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id，自增")
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 0)
    private Long id;

    @ApiModelProperty(value = "提单对应货柜信息id(ocean_counter id)")
    private Long oceanCounterId;

    @ApiModelProperty(value = "客户id(客户表 customer id)")
    private Long customerId;

    @ApiModelProperty(value = "订单对应箱号信息id(order_case id)")
    private Long orderCaseId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
