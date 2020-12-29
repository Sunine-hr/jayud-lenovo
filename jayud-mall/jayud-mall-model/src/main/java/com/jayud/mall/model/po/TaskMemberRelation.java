package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 任务成员关系表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TaskMemberRelation对象", description="任务成员关系表")
public class TaskMemberRelation extends Model<TaskMemberRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
