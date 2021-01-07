package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CabinetTypeForm {

    @ApiModelProperty(value = "柜型号名称", position = 1)
    @JSONField(ordinal = 1)
    private String name;

    @ApiModelProperty(value = "编码编码", position = 2)
    @JSONField(ordinal = 2)
    private String idCode;

}
