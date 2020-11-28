package com.jayud.mall.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationTeamMemberForm {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)")
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;


}
