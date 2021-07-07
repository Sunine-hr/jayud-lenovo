package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 提单(或柜子)对应费用明细
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "FeeCopeWith对象", description = "提单(或柜子)对应费用明细")
public class FeeCopeWith extends Model<FeeCopeWith> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提单id(ocean_bill id)/柜子id(ocean_counter id)")
    private Integer qie;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)")
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)")
    private String costName;

    @ApiModelProperty(value = "供应商id(supplier_info id)")
    private Integer supplierId;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公斤 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "来源(1计费重2固定)")
    private Integer source;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)")
    private Integer cid;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "业务类型(1提单费用 2柜子费用)")
    private Integer businessType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
