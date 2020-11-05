package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "模板对应模块信息Form")
public class TemplateFileForm {

    @ApiModelProperty(value = "自增id")
    @JSONField(ordinal = 0)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)")
    @JSONField(ordinal = 1)
    private Integer qie;

    @ApiModelProperty(value = "文件标题(quoted_file)")
    @JSONField(ordinal = 2)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    @JSONField(ordinal = 3)
    private Integer options;

    @ApiModelProperty(value = "描述")
    @JSONField(ordinal = 4)
    private String remarks;

}
