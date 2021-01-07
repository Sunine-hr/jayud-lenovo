package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "查询提单form")
public class QueryOceanBillForm extends BasePageForm{

    @ApiModelProperty(value = "提单号(供应商提供)", position = 1)
    @JSONField(ordinal = 1)
    private String orderId;

    @ApiModelProperty(value = "开船日期", position = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 2, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sailTime;

    @ApiModelProperty(value = "供应商id(supplier_info id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer supplierId;

    @ApiModelProperty(value = "目的地国家代码(country code)", position = 4)
    @JSONField(ordinal = 4)
    private String destinationCountryCode;

}
