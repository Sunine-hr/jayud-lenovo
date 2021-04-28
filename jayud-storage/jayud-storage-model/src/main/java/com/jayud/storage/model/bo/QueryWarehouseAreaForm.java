package com.jayud.storage.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QueryWarehouseAreaForm extends BasePageForm{

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "区域代码")
    private String areaCode;

    @ApiModelProperty(value = "仓库名称")
    private String name;

    @ApiModelProperty(value = "仓库代码")
    private String code;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
