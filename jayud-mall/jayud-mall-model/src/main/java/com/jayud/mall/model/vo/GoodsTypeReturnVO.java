package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsTypeReturnVO {

    @ApiModelProperty(value = "普货", position = 1)
    @JSONField(ordinal = 1)
    private List<GoodsTypeVO> generalCargo;

    @ApiModelProperty(value = "特货", position = 2)
    @JSONField(ordinal = 2)
    private List<GoodsTypeVO> specialCargo;
}
