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
 * 后台角色菜单关系表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemRoleMenuRelation对象", description="后台角色菜单关系表")
public class SystemRoleMenuRelation extends Model<SystemRoleMenuRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "角色ID", position = 2)
    @JSONField(ordinal = 2)
    private Long roleId;

    @ApiModelProperty(value = "菜单ID", position = 3)
    @JSONField(ordinal = 3)
    private Long menuId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
