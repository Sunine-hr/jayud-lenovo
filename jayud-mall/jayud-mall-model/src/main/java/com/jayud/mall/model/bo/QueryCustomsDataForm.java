package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报关资料表")
public class QueryCustomsDataForm extends BasePageForm {

    @ApiModelProperty(value = "IDcode，报关ID", position = 1)
    @JSONField(ordinal = 1)
    private String idCode;

    @ApiModelProperty(value = "报关英文品名", position = 2)
    @JSONField(ordinal = 2)
    private String enName;

    @ApiModelProperty(value = "报关HSCODE", position = 3)
    @JSONField(ordinal = 3)
    private String hsCode;



}
