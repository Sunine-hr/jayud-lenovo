package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SyncOrderForm {

    @ApiModelProperty(value = "运单号", position = 1)
    @JSONField(ordinal = 1)
    @NotNull(message = "运单号不能为空")
    private String shipmentId;

}
