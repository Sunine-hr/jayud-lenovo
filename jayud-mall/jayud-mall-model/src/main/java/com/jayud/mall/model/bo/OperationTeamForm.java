package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationTeamForm {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "小组代码")
    private String groupCode;

    @ApiModelProperty(value = "小组名称")
    private String groupName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

}
