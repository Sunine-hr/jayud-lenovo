package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TemplateCopeReceivableVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)", position = 3)
    @JSONField(ordinal = 3)
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)", position = 3)
    @JSONField(ordinal = 3)
    private String costName;

    @ApiModelProperty(value = "规格代码(quotation_type code;shipping_area warehouse_code)", position = 4)
    @JSONField(ordinal = 4)
    private String specificationCode;

    @ApiModelProperty(value = "规格名称(quotation_type name;shipping_area warehouse_name)", position = 4)
    @JSONField(ordinal = 4)
    private String specificationName;

    @ApiModelProperty(value = "计算方式(1自动 2手动)", position = 5)
    @JSONField(ordinal = 5)
    private Integer calculateWay;

    @ApiModelProperty(value = "数量", position = 6)
    @JSONField(ordinal = 6)
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公斤 2方 3票 4柜)", position = 7)
    @JSONField(ordinal = 7)
    private Integer unit;

    @ApiModelProperty(value = "数量来源(1计费重 2固定 3柜 4方)", position = 8)
    @JSONField(ordinal = 8)
    private Integer source;

    @ApiModelProperty(value = "单价", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency_info id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer cid;

    @ApiModelProperty(value = "总金额", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal amount;

    @ApiModelProperty(value = "描述", position = 12)
    @JSONField(ordinal = 12)
    private String remarks;

    /*currency_code	*/
    @ApiModelProperty(value = "币种代码", position = 13)
    @JSONField(ordinal = 13)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 14)
    @JSONField(ordinal = 14)
    private String currencyName;

    /*amountFormat*/
    @ApiModelProperty(value = "金额格式化", position = 15)
    @JSONField(ordinal = 15)
    private String amountFormat;

    @ApiModelProperty(value = "单价格式化", position = 16)
    @JSONField(ordinal = 16)
    private String unitPriceFormat;

    @ApiModelProperty(value = "区间最小值")
    private BigDecimal min;

    @ApiModelProperty(value = "区间最大值")
    private BigDecimal max;





}
