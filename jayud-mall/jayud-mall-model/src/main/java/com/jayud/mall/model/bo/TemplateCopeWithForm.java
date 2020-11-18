package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "报价对应应付费用明细Form")
public class TemplateCopeWithForm {

    @ApiModelProperty(value = "自增id")
    @JSONField(ordinal = 0)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)")
    @JSONField(ordinal = 1)
    private Integer qie;

    @ApiModelProperty(value = "费用名称")
    @JSONField(ordinal = 2)
    private String costName;

    @ApiModelProperty(value = "供应商代码")
    @JSONField(ordinal = 3)
    private String supplierCode;

    @ApiModelProperty(value = "计算方式(1自动 2手动)")
    @JSONField(ordinal = 4)
    private Integer calculateWay;

    @ApiModelProperty(value = "数量")
    @JSONField(ordinal = 5)
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公司 2方 3票 4柜)")
    @JSONField(ordinal = 6)
    private Integer unit;

    @ApiModelProperty(value = "来源(1计费重2固定)")
    @JSONField(ordinal = 7)
    private Integer source;

    @ApiModelProperty(value = "单价")
    @JSONField(ordinal = 8)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency表id)")
    @JSONField(ordinal = 9)
    private Integer cid;

    @ApiModelProperty(value = "总金额")
    @JSONField(ordinal = 10)
    private BigDecimal amount;

    @ApiModelProperty(value = "描述")
    @JSONField(ordinal = 11)
    private String remarks;

}
