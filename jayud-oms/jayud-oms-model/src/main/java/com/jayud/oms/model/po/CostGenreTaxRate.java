package com.jayud.oms.model.po;

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
 * 费用类型税率表
 * </p>
 *
 * @author LDR
 * @since 2021-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CostGenreTaxRate对象", description = "费用类型税率表")
public class CostGenreTaxRate extends Model<CostGenreTaxRate> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "费用类型id")
    private Long costGenreId;

    @ApiModelProperty(value = "费用类别id")
    private Long costTypeId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
