package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GoodsTypeReturnVO {

    @ApiModelProperty(value = "普货", position = 1)
    private List<GoodsTypeVO> generalCargo;

    @ApiModelProperty(value = "特货", position = 2)
    private List<GoodsTypeVO> specialCargo;
}
