package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "OrderConfCasesVO", description = "配载统计箱数")
@Data
public class OrderConfCasesVO {

    @ApiModelProperty(value = "总箱数")
    private Integer allboxNumber;

    @ApiModelProperty(value = "未配载箱数")
    private Integer notboxNumber;

    @ApiModelProperty(value = "已配箱数")
    private Integer hasboxNumber;

}
