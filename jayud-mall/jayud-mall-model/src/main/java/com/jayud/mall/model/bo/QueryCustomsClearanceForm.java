package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryCustomsClearanceForm extends BasePageForm {

    @ApiModelProperty(value = "IDcode，清关ID", position = 1)
    @JSONField(ordinal = 1)
    private String idCode;

    @ApiModelProperty(value = "清关国家代码", position = 2)
    @JSONField(ordinal = 2)
    private String customsCode;

    @ApiModelProperty(value = "清关HSCODE", position = 3)
    @JSONField(ordinal = 3)
    private String hsCode;

}
