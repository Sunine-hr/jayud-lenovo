package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CaseVO {

    //(客户预报)总重量
    @ApiModelProperty(value = "(客户预报)总重量 实际重")
    private BigDecimal totalAsnWeight;

    @ApiModelProperty(value = "客户预报的总材积重 材积重")
    private BigDecimal totalVolumeWeight;

    @ApiModelProperty(value = "客户预报的总收费重 收费重")
    private BigDecimal totalChargeWeight;

    //(客户预报)总体积
    @ApiModelProperty(value = "(客户预报)总体积 实际体积")
    private BigDecimal totalAsnVolume;

    //(客户预报)总箱数
    @ApiModelProperty(value = "(客户预报)总箱数 总箱数")
    private Integer totalCase;

    //箱号列表list
    @ApiModelProperty(value = "箱号列表list")
    List<OrderCaseVO> orderCaseVOList;

}
