package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "报价服务组Form")
@Data
public class ServiceGroupForm {

    @ApiModelProperty(value = "代码", position = 1)
    @JSONField(ordinal = 1)
    private String idCode;

    @ApiModelProperty(value = "名称", position = 2)
    @JSONField(ordinal = 2)
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 3)
    @JSONField(ordinal = 3)
    private String status;

}
