package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskMemberRelationForm {

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

}
