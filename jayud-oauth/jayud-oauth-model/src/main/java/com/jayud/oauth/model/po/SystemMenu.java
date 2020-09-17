package com.jayud.oauth.model.po;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 后台菜单表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemMenu对象", description="后台菜单表")
public class SystemMenu extends Model<SystemMenu> {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单级数")
    private Integer level;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    private String title;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端隐藏")
    private Integer hidden;

    @ApiModelProperty(value = "前端路由，二级菜单链接地址")
    private String router;

    @ApiModelProperty(value = "名称key值")
    private String name;

    @ApiModelProperty(value = "创建人")
    private String createdUser;

    @ApiModelProperty(value = "修改人")
    private String updatedUser;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
