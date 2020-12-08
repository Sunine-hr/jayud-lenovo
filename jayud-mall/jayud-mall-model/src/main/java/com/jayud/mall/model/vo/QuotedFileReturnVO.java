package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuotedFileReturnVO {

    @ApiModelProperty(value = "类型名称 1报关服务 2清关服务", position = 1)
    private String typesName;

    @ApiModelProperty(value = "分组名称", position = 2)
    private String groupName;

    @ApiModelProperty(value = "文件标题(quoted_file file_name)", position = 3)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 4)
    private Integer options;

    @ApiModelProperty(value = "描述", position = 5)
    private String remarks;

    @ApiModelProperty(value = "报价对应的文件id(quoted_file id)")
    private Long qfId;

}
