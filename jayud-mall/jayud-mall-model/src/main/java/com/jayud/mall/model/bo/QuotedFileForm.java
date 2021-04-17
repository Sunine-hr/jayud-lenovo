package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "报价对应的文件表Form")
public class QuotedFileForm {

    @ApiModelProperty(value = "自增加id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "文件分组代码", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "文件代码", position = 3)
    @JSONField(ordinal = 3)
    private String idCode;

    @ApiModelProperty(value = "文件名称", position = 4)
    @JSONField(ordinal = 4)
    private String fileName;

    @ApiModelProperty(value = "是否必要(0否 1是)", position = 5)
    @JSONField(ordinal = 5)
    private Integer options;

    @ApiModelProperty(value = "是否审核(0否 1是)", position = 6)
    @JSONField(ordinal = 6)
    private Integer isCheck;

    @ApiModelProperty(value = "说明", position = 7)
    @JSONField(ordinal = 7)
    private String describe;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 8)
    @JSONField(ordinal = 9)
    private String status;

}
