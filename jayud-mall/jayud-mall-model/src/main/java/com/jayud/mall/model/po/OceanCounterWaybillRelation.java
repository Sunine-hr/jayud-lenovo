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
 * 货柜对应运单(订单)关联表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanCounterWaybillRelation对象", description="货柜对应运单(订单)关联表")
public class OceanCounterWaybillRelation extends Model<OceanCounterWaybillRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "货柜id(ocean_counter id)", position = 2)
    @JSONField(ordinal = 2)
    private Long oceanCounterId;

    @ApiModelProperty(value = "运单id(订单id)(order_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Long orderInfoId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
