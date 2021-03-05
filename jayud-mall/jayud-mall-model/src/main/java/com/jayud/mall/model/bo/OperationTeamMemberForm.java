package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperationTeamMemberForm {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 2)
    @JSONField(ordinal = 2)
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)", position = 3)
    @JSONField(ordinal = 3)
    private Long memberUserId;

    @ApiModelProperty(value = "任务id(task id)", position = 4)
    @JSONField(ordinal = 4)
    private Long taskId;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @JSONField(ordinal = 5)
    private String status;

}
