package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 进仓单号表(取单号)
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "IntoWarehouseNumber对象", description = "进仓单号表(取单号)")
public class IntoWarehouseNumber extends Model<IntoWarehouseNumber> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "进仓单号")
    private String warehouseNo;

    @ApiModelProperty(value = "使用状态(0未使用 1已使用)")
    private Integer usageStatus;


    @Override
    protected Serializable pkVal() {
        return this.warehouseNo;
    }

}
