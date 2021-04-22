package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuotationTypeReturnVO {

//    @ApiModelProperty(value = "整柜", position = 1)
//    @JSONField(ordinal = 1)
//    private List<QuotationTypeVO> fullContainer;
//
//    @ApiModelProperty(value = "散柜", position = 2)
//    @JSONField(ordinal = 2)
//    private List<QuotationTypeVO> scatteredArk;

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private String id;

    @ApiModelProperty(value = "报价类型名称", position = 2)
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "子节点", position = 11)
    @JSONField(ordinal = 3)
    private List<QuotationTypeVO> children;





}
