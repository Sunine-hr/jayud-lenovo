package com.jayud.mall.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@ApiModel(value="OperationTeamMember对象", description="运营(服务)小组成员")
public class OperationTeamMember extends Model<OperationTeamMember> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "运营(服务)小组id(operation_team id)")
    private Long operationTeamId;

    @ApiModelProperty(value = "成员用户Id(system_user id)")
    private Long memberUserId;

    @ApiModelProperty(value = "状态(0无效 1有效)")
    private String status;

    @ApiModelProperty(value = "创建人")
    private Long creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creationTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
