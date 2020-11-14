package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TemplateCopeWithVO {

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


}
