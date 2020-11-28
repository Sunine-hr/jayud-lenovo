package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskMemberRelationVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "任务类型(1提单任务 2运单任务)")
    private String taskType;

    @ApiModelProperty(value = "任务id(提单任务列表 bill_task id 运单任务列表 waybill_task id)")
    private Long taskId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户id(system_user id)")
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creationTime;

    /*提单任务*/
    @ApiModelProperty(value = "提单任务-代码")
    private String billTaskCode;

    @ApiModelProperty(value = "提单任务-名称")
    private String billTaskName;

    /*运单任务*/
    @ApiModelProperty(value = "运单任务-代码")
    private String waybillTaskCode;

    @ApiModelProperty(value = "运单任务-名称")
    private String waybillTaskName;

    /*任务的运营小组*/
    @ApiModelProperty(value = "运营小组-代码")
    private String operationGroupCode;

    @ApiModelProperty(value = "运营小组-名称")
    private String operationGroupName;

    /*任务小组具体执行人*/
    @ApiModelProperty(value = "任务-对应-运营小组-具体执行人")
    private String memberUserName;

}
