package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TemplateCopeWithVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "费用名称", position = 3)
    @JSONField(ordinal = 3)
    private String costName;

    @ApiModelProperty(value = "供应商代码", position = 4)
    @JSONField(ordinal = 4)
    private String supplierCode;

    @ApiModelProperty(value = "计算方式(1自动 2手动)", position = 5)
    @JSONField(ordinal = 5)
    private Integer calculateWay;

    @ApiModelProperty(value = "数量", position = 6)
    @JSONField(ordinal = 6)
    private Integer count;

    @ApiModelProperty(value = "数量单位(1公司 2方 3票 4柜)", position = 7)
    @JSONField(ordinal = 7)
    private Integer unit;

    @ApiModelProperty(value = "来源(1计费重2固定)", position = 8)
    @JSONField(ordinal = 8)
    private Integer source;

    @ApiModelProperty(value = "单价", position = 9)
    @JSONField(ordinal = 9)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "币种(currency表id)", position = 10)
    @JSONField(ordinal = 10)
    private Integer cid;

    @ApiModelProperty(value = "总金额", position = 11)
    @JSONField(ordinal = 11)
    private BigDecimal amount;

    @ApiModelProperty(value = "描述", position = 12)
    @JSONField(ordinal = 12)
    private String remarks;


}
