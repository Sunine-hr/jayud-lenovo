package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillTaskVO {

    @ApiModelProperty(value = "自增id")
    private Long id;

    @ApiModelProperty(value = "分组代码(task_group id_code)")
    private String groupCode;

    @ApiModelProperty(value = "任务代码")
    private String taskCode;

    @ApiModelProperty(value = "任务名")
    private String taskName;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "天数")
    private Integer days;

    @ApiModelProperty(value = "天数标识(ETD(开船时间，正数就是加2))和天数组合使用")
    private String dayFlag;

    @ApiModelProperty(value = "执行人")
    private String operators;

    @ApiModelProperty(value = "完成这个任务所需的分钟数")
    private Integer minutes;

    @ApiModelProperty(value = "完成这个任务的考核得分")
    private Integer score;

    @ApiModelProperty(value = "描述")
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "创建人名字")
    private String userName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*任务分组:task_group*/
    @ApiModelProperty(value = "分组代码")
    private String idCode;

    @ApiModelProperty(value = "分组名")
    private String codeName;

    /*提单表:ocean_bill*/
    //...

    /*运营(服务)小组:operation_team*/
    @ApiModelProperty(value = "运营(服务)小组id")
    private Long operationTeamId;

    /*任务对应运营小组成员*/
    @ApiModelProperty(value = "成员id")
    private Long memberUserId;

    @ApiModelProperty(value = "成员名称")
    private String memberUserName;




}
