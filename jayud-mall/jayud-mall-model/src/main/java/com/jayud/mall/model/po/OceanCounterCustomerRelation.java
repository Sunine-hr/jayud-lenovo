package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
    private Long id;

    @ApiModelProperty(value = "提单对应货柜信息id(ocean_counter id)")
    private Long oceanCounterId;

    @ApiModelProperty(value = "客户id(客户表 customer id)")
    private Long customerId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
