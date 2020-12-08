package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuotationTypeReturnVO {

    @ApiModelProperty(value = "整柜", position = 1)
    private List<QuotationTypeVO> fullContainer;

    @ApiModelProperty(value = "散柜", position = 2)
    private List<QuotationTypeVO> scatteredArk;

}
