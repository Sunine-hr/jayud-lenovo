package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskGroupVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "类型(1提单任务分组 2运单任务分组)", position = 2)
    @JSONField(ordinal = 2)
    private Integer types;

    @ApiModelProperty(value = "分组代码", position = 3)
    @JSONField(ordinal = 3)
    private String groupCode;

    @ApiModelProperty(value = "分组名称", position = 4)
    @JSONField(ordinal = 4)
    private String groupName;

    @ApiModelProperty(value = "描述", position = 5)
    @JSONField(ordinal = 5)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 6)
    @JSONField(ordinal = 6)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 7)
    @JSONField(ordinal = 7)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 8)
    @JSONField(ordinal = 8)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 9)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 9, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //任务事项
    @ApiModelProperty(value = "任务事项", position = 10)
    @JSONField(ordinal = 10)
    private String taskItems;

    //任务项明细(1提单任务项明细 2运单任务项明细)
    // BillTask 提单任务列表
    // WaybillTask 运单(订单)任务列表
    @ApiModelProperty(value = "任务项明细", position = 11)
    @JSONField(ordinal = 11)
    private List<TaskItemVO> taskItemVOS;

}
