package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationTeamMemberVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 2)
    @JSONField(ordinal = 2)
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)", position = 3)
    @JSONField(ordinal = 3)
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 4)
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)", position = 5)
    @JSONField(ordinal = 5)
    private Long creator;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    /*关联信息*/
    @ApiModelProperty(value = "运营(服务)小组-名称", position = 7)
    @JSONField(ordinal = 7)
    private String operationTeamName;

    @ApiModelProperty(value = "运营(服务)小组成员-名称", position = 8)
    @JSONField(ordinal = 8)
    private String memberUserName;


}
