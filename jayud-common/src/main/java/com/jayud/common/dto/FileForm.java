package com.jayud.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author ciro
 * @date 2022/1/11 9:53
 * @description: 文件对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "文件对象", description = "文件对象")
public class FileForm {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "绝对路径")
    private String absolutePath;

    @ApiModelProperty(value = "相对路径")
    private String relativePath;

    @ApiModelProperty(value = "缩略图路径")
    private String thumbFilePath;
}
