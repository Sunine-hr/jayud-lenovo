package com.jayud.finance.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SheetHeadVO {

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "表头显示值")
    private String viewName;





}
