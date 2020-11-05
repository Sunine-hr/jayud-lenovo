package com.jayud.tools.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CargoNameSmallVO {

    @ApiModelProperty(value = "圆通单号")
    private String ytdh;

    @ApiModelProperty(value = "重量(PCS)")
    private BigDecimal zl;

    @ApiModelProperty(value = "收货人")
    private String xm1;

    @ApiModelProperty(value = "件数")
    private Integer js;

//    @ApiModelProperty(value = "(原)货品名称")
//    private String hpmc;
//
//    @ApiModelProperty(value = "替换名称")
//    private String replaceName;

    @ApiModelProperty(value = "货品名称")
    private String showname;

}
