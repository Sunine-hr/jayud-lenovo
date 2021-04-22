package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TemplateFileOrderForm {

    @ApiModelProperty(value = "报价id(offer_info id)", position = 1)
    private Integer offerInfoId;

    @ApiModelProperty(value = "文件分组代码" +
            "A,报关服务-买单报关" +
            "B,报关服务-独立报关" +
            "C,清关服务-买单报关" +
            "D,清关服务-独立报关", position = 2)
    private String groupCode;

}
