package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BreakoutWorkbenchForm {

    @ApiModelProperty(value = "工作台id")
    private Long workbenchId;

    @ApiModelProperty(value = "编号,播种墙编号")
    private String seedingWallCode;
}
