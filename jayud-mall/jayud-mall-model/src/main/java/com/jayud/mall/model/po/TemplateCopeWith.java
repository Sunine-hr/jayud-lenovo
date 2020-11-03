package com.jayud.mall.model.po;

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
 * 报价对应应付费用明细
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TemplateCopeWith对象", description="报价对应应付费用明细")
public class TemplateCopeWith extends Model<TemplateCopeWith> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)")
    private Integer qie;

    @ApiModelProperty(value = "费用名称")
    private String costName;

    @ApiModelProperty(value = "供应商代码")
    private String supplierCode;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公司 2方 3票 4柜)")
    private Integer unit;

    @ApiModelProperty(value = "来源(1计费重2固定)")
    private Integer source;

    @ApiModelProperty(value = "单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency表id)")
    private Integer cid;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "描述")
    private String remarks;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
