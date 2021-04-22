package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("机场、港口信息")
@Data
public class HarbourInfoForm {

    @ApiModelProperty(value = "代码", position = 1)
    @JSONField(ordinal = 1)
    private String idCode;

    @ApiModelProperty(value = "中文名称", position = 2)
    @JSONField(ordinal = 2)
    private String codeName;

    @ApiModelProperty(value = "英文名称", position = 3)
    @JSONField(ordinal = 3)
    private String codeNameEn;

    @ApiModelProperty(value = "国家代码", position = 4)
    @JSONField(ordinal = 4)
    private String stateCode;

    @ApiModelProperty(value = "类型(1机场 2港口)", position = 5)
    @JSONField(ordinal = 5)
    private Integer genre;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 6)
    @JSONField(ordinal = 6)
    private String status;

}
