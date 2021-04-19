package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户对应角色
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemUserRoleRelation对象", description="用户对应角色")
public class SystemUserRoleRelation extends Model<SystemUserRoleRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "用户ID", position = 2)
    @JSONField(ordinal = 2)
    private Integer userId;

    @ApiModelProperty(value = "角色ID", position = 3)
    @JSONField(ordinal = 3)
    private Integer roleId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
