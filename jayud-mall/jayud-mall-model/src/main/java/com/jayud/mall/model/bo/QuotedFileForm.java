package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报价对应的文件表Form")
public class QuotedFileForm {

    @ApiModelProperty(value = "自增加id")
    private Long id;

    @ApiModelProperty(value = "文件分组代码")
    private String groupCode;

    @ApiModelProperty(value = "文件代码")
    private String idCode;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)")
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)")
    private Integer isCheck;

    @ApiModelProperty(value = "说明")
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

}
