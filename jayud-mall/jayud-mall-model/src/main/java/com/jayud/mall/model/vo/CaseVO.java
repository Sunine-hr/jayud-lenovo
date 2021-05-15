package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CaseVO {

    //(客户预报)总重量
    @ApiModelProperty(value = "(客户预报)总重量")
    private BigDecimal totalAsnWeight;

    //(客户预报)总体积
    @ApiModelProperty(value = "(客户预报)总体积")
    private BigDecimal totalAsnVolume;

    //(客户预报)总箱数
    @ApiModelProperty(value = "(客户预报)总箱数")
    private Integer totalCase;

    //箱号列表list
    @ApiModelProperty(value = "箱号列表list")
    List<OrderCaseVO> orderCaseVOList;

}
