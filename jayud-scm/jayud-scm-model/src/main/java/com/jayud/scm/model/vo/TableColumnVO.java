package com.jayud.scm.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "表格列对象")
@Data
public class TableColumnVO {

    //动态列设置
    @ApiModelProperty(value = "列属性")
    private String columnProp;

    @ApiModelProperty(value = "列标签")
    private String columnLabel;

    @ApiModelProperty(value = "列宽度（px）")
    private String columnWidth;

    @ApiModelProperty(value = "列排序")
    private Integer columnSort;

    @ApiModelProperty(value = "列状态（1显示 2不显示）")
    private String columnStatus;

}
