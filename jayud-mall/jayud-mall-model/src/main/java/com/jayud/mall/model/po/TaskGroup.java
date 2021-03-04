package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 提单任务分组
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TaskGroup", description="任务分组")
public class TaskGroup extends Model<TaskGroup> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(value = "`status`")
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
