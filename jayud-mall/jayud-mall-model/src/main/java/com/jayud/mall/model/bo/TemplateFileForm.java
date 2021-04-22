package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "模板对应模块信息Form")
public class TemplateFileForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "报价模板id(quotation_template)", position = 2)
    @JSONField(ordinal = 2)
    private Integer qie;

    @ApiModelProperty(value = "文件标题(quoted_file)", position = 3)
    @JSONField(ordinal = 3)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 4)
    @JSONField(ordinal = 4)
    private Integer options;

    @ApiModelProperty(value = "描述", position = 5)
    @JSONField(ordinal = 5)
    private String remarks;

    @ApiModelProperty(value = "报价对应的文件id(quoted_file id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer qfId;

}
