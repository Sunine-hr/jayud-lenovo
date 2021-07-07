package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 任务执行规则表(from where to where)
 * </p>
 *
 * @author fachang.mao
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TaskExecutionRule对象", description = "任务执行规则表(from where to where)")
public class TaskExecutionRule extends Model<TaskExecutionRule> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "from任务类型(task types)")
    private Integer fromTaskType;

    @ApiModelProperty(value = "from任务id(task id)")
    private Long fromTaskId;

    @ApiModelProperty(value = "from任务代码(task task_code)")
    private String fromTaskCode;

    @ApiModelProperty(value = "from任务名称(task task_name)")
    private String fromTaskName;

    @ApiModelProperty(value = "to任务类型(task types)")
    private Integer toTaskType;

    @ApiModelProperty(value = "to任务id(task id)")
    private Long toTaskId;

    @ApiModelProperty(value = "to任务代码(task task_code)")
    private String toTaskCode;

    @ApiModelProperty(value = "to任务名称(task task_name)")
    private String toTaskName;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
