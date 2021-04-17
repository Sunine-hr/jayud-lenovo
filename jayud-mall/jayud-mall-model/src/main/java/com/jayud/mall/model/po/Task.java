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
 * 基础任务表
 * </p>
 *
 * @author fachang.mao
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Task对象", description="基础任务表")
public class Task extends Model<Task> {

    private static final long serialVersionUID = 1L;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
