package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "图片数据")
public class PicUrlArrForm {

    @ApiModelProperty(value = "图片-文件路径")
    private String filePath;

}
