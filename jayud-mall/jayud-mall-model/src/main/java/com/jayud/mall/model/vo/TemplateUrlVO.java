package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "TemplateUrlVO", description = "模板urlVO")
@Data
public class TemplateUrlVO {

    @ApiModelProperty(value = "文件名称", position = 1)
    @JSONField(ordinal = 1)
    private String fileName;

    @ApiModelProperty(value = "相对路径", position = 2)
    @JSONField(ordinal = 2)
    private String relativePath;

    @ApiModelProperty(value = "绝对路径", position = 3)
    @JSONField(ordinal = 3)
    private String absolutePath;

}
