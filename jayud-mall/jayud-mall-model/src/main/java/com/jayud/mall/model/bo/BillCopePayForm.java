package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillCopePayForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "提单id(ocean_bill id)", position = 2)
    @JSONField(ordinal = 2)
    private Long billId;

    @ApiModelProperty(value = "费用代码(cost_item cost_code)", position = 3)
    @JSONField(ordinal = 3)
    private String costCode;

    @ApiModelProperty(value = "费用名称(cost_item cost_name)", position = 4)
    @JSONField(ordinal = 4)
    private String costName;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 5)
    @JSONField(ordinal = 5)
    private Integer supplierId;

    @ApiModelProperty(value = "金额", position = 6)
    @JSONField(ordinal = 6)
    private BigDecimal amount;

    @ApiModelProperty(value = "币种(currency_info id)", position = 7)
    @JSONField(ordinal = 7)
    private Integer cid;

    @ApiModelProperty(value = "描述", position = 8)
    @JSONField(ordinal = 8)
    private String remarks;

    //币种
    @ApiModelProperty(value = "币种代码", position = 9)
    @JSONField(ordinal = 9)
    private String currencyCode;

    @ApiModelProperty(value = "币种名称", position = 10)
    @JSONField(ordinal = 10)
    private String currencyName;

}
