package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "任务代码", position = 2)
    @JSONField(ordinal = 2)
    private String taskCode;

    @ApiModelProperty(value = "任务名称", position = 3)
    @JSONField(ordinal = 3)
    private String taskName;

    @ApiModelProperty(value = "描述", position = 4)
    @JSONField(ordinal = 4)
    private String remarks;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 6)
    @JSONField(ordinal = 6)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 7)
    @JSONField(ordinal = 7)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 10)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 10, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
