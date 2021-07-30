package com.jayud.scm.model.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 后台用户角色表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemRole对象", description="后台用户角色表")
public class SystemRole extends Model<SystemRole> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "后台用户数量")
    private Integer adminCount;

    @ApiModelProperty(value = "启用状态：0->禁用；1->启用")
    private Integer status;

    @ApiModelProperty(value = "角色优先级")
    private Integer sort;

    @ApiModelProperty(value = "仅供前台使用")
    private String webFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
