package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "报价服务组Form")
@Data
public class ServiceGroupForm {

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

}
