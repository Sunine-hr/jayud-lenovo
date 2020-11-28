package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationTeamMemberVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)")
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creationTime;

    /*关联信息*/
    @ApiModelProperty(value = "运营(服务)小组-名称")
    private String operationTeamName;

    @ApiModelProperty(value = "运营(服务)小组成员-名称")
    private String memberUserName;


}
