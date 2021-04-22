package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySupplierServeForm extends BasePageForm{

    @ApiModelProperty(value = "服务名", position = 1)
    @JSONField(ordinal = 1)
    private String serveName;

    @ApiModelProperty(value = "服务类型", position = 2)
    @JSONField(ordinal = 2)
    private Long serviceTypeId;

    @ApiModelProperty(value = "供应商", position = 3)
    @JSONField(ordinal = 3)
    private Long supplierInfoId;

    @ApiModelProperty(value = "费用项目", position = 4)
    @JSONField(ordinal = 4)
    private Long costItemId;


}
