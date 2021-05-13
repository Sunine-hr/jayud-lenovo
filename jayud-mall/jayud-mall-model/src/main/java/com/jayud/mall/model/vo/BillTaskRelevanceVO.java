package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillTaskRelevanceVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "提单id (ocean_bill id)", position = 2)
    @JSONField(ordinal = 2)
    private Long oceanBillId;

    @ApiModelProperty(value = "任务代码(bill_task task_code)", position = 3)
    @JSONField(ordinal = 3)
    private String taskCode;

    @ApiModelProperty(value = "任务名(bill_task task_name)", position = 4)
    @JSONField(ordinal = 4)
    private String taskName;

    @ApiModelProperty(value = "排序值", position = 5)
    @JSONField(ordinal = 5)
    private Integer sort;

    @ApiModelProperty(value = "天数", position = 6)
    @JSONField(ordinal = 6)
    private Integer days;

    @ApiModelProperty(value = "天数标识", position = 7)
    @JSONField(ordinal = 7)
    private String dayFlag;

    @ApiModelProperty(value = "执行人", position = 8)
    @JSONField(ordinal = 8)
    private String operators;

    @ApiModelProperty(value = "完成这个任务所需的分钟数", position = 9)
    @JSONField(ordinal = 9)
    private String minutes;

    @ApiModelProperty(value = "完成这个任务的考核得分", position = 10)
    @JSONField(ordinal = 10)
    private Integer score;

    @ApiModelProperty(value = "物流轨迹通知描述", position = 11)
    @JSONField(ordinal = 11)
    private String remarks;

    @ApiModelProperty(value = "状态(0未激活 1已激活,未完成 2已完成)", position = 12)
    @JSONField(ordinal = 12)
    private String status;

    @ApiModelProperty(value = "延期原因", position = 13)
    @JSONField(ordinal = 13)
    private String reason;

    @ApiModelProperty(value = "延期/完成时间", position = 14)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 14, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime upTime;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 15)
    @JSONField(ordinal = 15)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 16)
    @JSONField(ordinal = 16)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 17)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 17, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //操作说明
    @ApiModelProperty(value = "操作说明", position = 18)
    @JSONField(ordinal = 18)
    private String operateDescribes;


}
