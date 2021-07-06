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
 * 海关基础资料-价值表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "CustomsBaseValue对象", description = "海关基础资料-价值表")
public class CustomsBaseValue extends Model<CustomsBaseValue> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "类型(1报关 2清关)")
    private Integer type;

    @ApiModelProperty(value = "海关基础资料id(customs_data id customs_clearance id)")
    private Long customsId;

    @ApiModelProperty(value = "国家代码(country code)")
    private String countryCode;

    @ApiModelProperty(value = "国家名称(country name)")
    private String countryName;

    @ApiModelProperty(value = "申报价值")
    private BigDecimal declaredValue;

    @ApiModelProperty(value = "申报价值的货币单位(currency_info currency_code)")
    private String declaredCurrency;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
