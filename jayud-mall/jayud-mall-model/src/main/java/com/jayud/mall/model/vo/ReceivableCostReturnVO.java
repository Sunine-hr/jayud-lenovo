package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceivableCostReturnVO {

    @ApiModelProperty(value = "名称")
    private String costName;

}
