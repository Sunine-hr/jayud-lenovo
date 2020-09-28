package com.jayud.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class FileView {

    @ApiModelProperty(value = "绝对路径")
    private String absolutePath;

    @ApiModelProperty(value = "相对路径")
    private String relativePath;

    @ApiModelProperty(value = "文件名称")
    private String fileName;


}
