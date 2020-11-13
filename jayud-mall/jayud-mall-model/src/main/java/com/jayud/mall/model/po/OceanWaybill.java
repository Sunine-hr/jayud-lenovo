package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 运单表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OceanWaybill对象", description="运单表")
public class OceanWaybill extends Model<OceanWaybill> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运单号")
    private String waybillNo;

    @ApiModelProperty(value = "描述")
    @TableField(value = "`describe`")
    private String describe;

    @ApiModelProperty(value = "送货信息")
    private String shippingInformation;

    @ApiModelProperty(value = "货柜信息id(ocean_counter id)")
    private Integer oceanCounterId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
