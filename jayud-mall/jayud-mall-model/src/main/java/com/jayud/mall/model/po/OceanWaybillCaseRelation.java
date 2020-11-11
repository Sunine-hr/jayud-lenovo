package com.jayud.mall.model.po;

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
 * 运单对应箱号关联表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanWaybillCaseRelation对象", description="运单对应箱号关联表")
public class OceanWaybillCaseRelation extends Model<OceanWaybillCaseRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id，自增")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单id(ocean_waybill id)")
    private Long oceanWaybillId;

    @ApiModelProperty(value = "订单对应箱号信息id(order_case id)")
    private Long orderCaseId;

    @ApiModelProperty(value = "客户id(customer id)")
    private Long customerId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
