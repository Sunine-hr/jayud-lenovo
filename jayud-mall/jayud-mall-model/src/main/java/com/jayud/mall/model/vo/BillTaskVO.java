package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillTaskVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "分组代码(task_group group_code)", position = 2)
    @JSONField(ordinal = 2)
    private String groupCode;

    @ApiModelProperty(value = "任务代码(task task_code)", position = 3)
    @JSONField(ordinal = 3)
    private String taskCode;

    @ApiModelProperty(value = "任务名称(task task_name)", position = 4)
    @JSONField(ordinal = 4)
    private String taskName;

    @ApiModelProperty(value = "排序值", position = 5)
    @JSONField(ordinal = 5)
    private Integer sort;

    @ApiModelProperty(value = "天数", position = 6)
    @JSONField(ordinal = 6)
    private Integer days;

    @ApiModelProperty(value = "天数标识(ETD(开船时间，正数就是加2))和天数组合使用", position = 7)
    @JSONField(ordinal = 7)
    private String dayFlag;

    @ApiModelProperty(value = "执行人", position = 8)
    @JSONField(ordinal = 8)
    private String operators;

    @ApiModelProperty(value = "完成这个任务所需的分钟数", position = 9)
    @JSONField(ordinal = 9)
    private Integer minutes;

    @ApiModelProperty(value = "完成这个任务的考核得分", position = 10)
    @JSONField(ordinal = 10)
    private Integer score;

    @ApiModelProperty(value = "物流轨迹通知描述", position = 11)
    @JSONField(ordinal = 11)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 12)
    @JSONField(ordinal = 12)
    private String status;

    @ApiModelProperty(value = "创建人id(system_user id)", position = 13)
    @JSONField(ordinal = 13)
    private Integer userId;

    @ApiModelProperty(value = "创建人名字(system_user name)", position = 14)
    @JSONField(ordinal = 14)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 15)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 15, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "激活开关(0未激活 1已激活)")
    private String activationSwitch;

    /*任务分组:task_group*/
    @ApiModelProperty(value = "分组名称", position = 16)
    @JSONField(ordinal = 16)
    private String groupName;

    /*提单表:ocean_bill*/
    //...

    /*运营(服务)小组:operation_team*/
    @ApiModelProperty(value = "运营(服务)小组id", position = 17)
    @JSONField(ordinal = 17)
    private Long operationTeamId;

    /*任务对应运营小组成员*/
    @ApiModelProperty(value = "成员id", position = 18)
    @JSONField(ordinal = 18)
    private Long memberUserId;

    @ApiModelProperty(value = "成员名称", position = 19)
    @JSONField(ordinal = 19)
    private String memberUserName;




}
