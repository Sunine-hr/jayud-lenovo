package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryAccountPayableForm extends BasePageForm{

    @ApiModelProperty(value = "对账单编号", position = 1)
    @JSONField(ordinal = 1)
    private String dzdNo;

    @ApiModelProperty(value = "法人id(legal_person id)", position = 2)
    @JSONField(ordinal = 2)
    private Long legalPersonId;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "状态(0未付款 1已付款)", position = 4)
    @JSONField(ordinal = 4)
    private Integer status;


}
