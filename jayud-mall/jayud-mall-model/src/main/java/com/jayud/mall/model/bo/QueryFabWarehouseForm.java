package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "查询报价模板参数")
public class QueryFabWarehouseForm extends BasePageForm{

    @ApiModelProperty(value = "仓库代码", position = 1)
    @JSONField(ordinal = 1)
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称", position = 2)
    @JSONField(ordinal = 2)
    private String warehouseName;

    @ApiModelProperty(value = "国家代码", position = 3)
    @JSONField(ordinal = 3)
    private String stateCode;
}
