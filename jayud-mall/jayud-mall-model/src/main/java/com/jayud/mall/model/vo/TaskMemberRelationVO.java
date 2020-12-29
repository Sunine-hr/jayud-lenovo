package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskMemberRelationVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "任务类型(1提单任务 2运单任务)", position = 2)
    @JSONField(ordinal = 2)
    private String taskType;

    @ApiModelProperty(value = "任务id(提单任务列表 bill_task id 运单任务列表 waybill_task id)", position = 3)
    @JSONField(ordinal = 3)
    private Long taskId;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 4)
    @JSONField(ordinal = 4)
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户id(system_user id)", position = 5)
    @JSONField(ordinal = 5)
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 6)
    @JSONField(ordinal = 6)
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private Long creator;

    @ApiModelProperty(value = "创建时间", position = 8)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 8, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    /*提单任务*/
    @ApiModelProperty(value = "提单任务-代码", position = 9)
    @JSONField(ordinal = 9)
    private String billTaskCode;

    @ApiModelProperty(value = "提单任务-名称", position = 10)
    @JSONField(ordinal = 10)
    private String billTaskName;

    /*运单任务*/
    @ApiModelProperty(value = "运单任务-代码", position = 11)
    @JSONField(ordinal = 11)
    private String waybillTaskCode;

    @ApiModelProperty(value = "运单任务-名称", position = 12)
    @JSONField(ordinal = 12)
    private String waybillTaskName;

    /*任务的运营小组*/
    @ApiModelProperty(value = "运营小组-代码", position = 13)
    @JSONField(ordinal = 13)
    private String operationGroupCode;

    @ApiModelProperty(value = "运营小组-名称", position = 14)
    @JSONField(ordinal = 14)
    private String operationGroupName;

    /*任务小组具体执行人*/
    @ApiModelProperty(value = "任务-对应-运营小组-具体执行人", position = 15)
    @JSONField(ordinal = 15)
    private String memberUserName;

}
