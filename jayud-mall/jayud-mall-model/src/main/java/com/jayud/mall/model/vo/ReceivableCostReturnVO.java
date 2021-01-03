package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceivableCostReturnVO {

    @ApiModelProperty(value = "名称", position = 1)
    @JSONField(ordinal = 1)
    private String costName;

}
