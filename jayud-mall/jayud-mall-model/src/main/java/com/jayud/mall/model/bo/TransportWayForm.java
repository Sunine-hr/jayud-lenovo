package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "运输方式Form")
@Data
public class TransportWayForm {

    @ApiModelProperty(value = "代码")
    private String idCode;

    @ApiModelProperty(value = "名称")
    private String codeName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

}
