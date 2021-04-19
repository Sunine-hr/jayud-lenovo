package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PicUrlArrForm")
public class PicUrlArrForm {

    @ApiModelProperty(value = "图片-文件路径", position = 1)
    @JSONField(ordinal = 1)
    private String filePath;

}
