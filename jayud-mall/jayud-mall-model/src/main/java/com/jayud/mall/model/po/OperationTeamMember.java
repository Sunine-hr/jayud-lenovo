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
 * 运营(服务)小组成员
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="OperationTeamMember", description="运营(服务)小组成员")
public class OperationTeamMember extends Model<OperationTeamMember> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)", position = 2)
    @JSONField(ordinal = 2)
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)", position = 3)
    @JSONField(ordinal = 3)
    private Long memberUserId;

    @ApiModelProperty(value = "任务id(task id)", position = 4)
    @JSONField(ordinal = 4)
    private Long taskId;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 5)
    @JSONField(ordinal = 5)
    private String status;

    @ApiModelProperty(value = "创建人(system_user id)", position = 6)
    @JSONField(ordinal = 6)
    private Long creator;

    @ApiModelProperty(value = "创建时间", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
