package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPayBillMasterForm extends BasePageForm {

    @ApiModelProperty(value = "账单编号(编码)", position = 1)
    @JSONField(ordinal = 1)
    private String billCode;

    @ApiModelProperty(value = "订单编号", position = 2)
    @JSONField(ordinal = 2)
    private String orderNo;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "账单状态(0未付款 1已付款)", position = 4)
    @JSONField(ordinal = 4)
    private Integer status;

}
