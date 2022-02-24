package com.jayud.auth.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author jayud.dev
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysMenu对象", description="系统菜单表")
public class SysMenu extends SysBaseEntity {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "id")
//      @TableId(value = "id", type = IdType.AUTO)
//    private Long id;

    @ApiModelProperty(value = "父级ID")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称")
    private String title;

    @ApiModelProperty(value = "菜单级数")
    private Integer level;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    private String name;

    @ApiModelProperty(value = "前端编码")
    private String code;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端隐藏")
    private Integer hidden;

    @ApiModelProperty(value = "前端路由")
    private String router;

    @ApiModelProperty(value = "菜单类型(zgys,ky,bg,hy,nl)")
    private String type;

    @ApiModelProperty(value = "是否路由菜单: 0-不是  1-是（默认值1）")
    private Boolean isRoute;

    @ApiModelProperty(value = "是否叶子节点:1-是   0-不是")
    private Boolean isLeaf;

    @ApiModelProperty(value = "是否按钮:1-是   0-不是")
    private Boolean isButton;

    @ApiModelProperty(value = "是否审核:1-是   0-不是")
    private Boolean isApprove;

    @ApiModelProperty(value = "系统类型(1-权限系统，2-OMS系统)")
    private Integer sysType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    private Boolean isDeleted;

//    @ApiModelProperty(value = "创建人")
//    private String createBy;
//
//    @ApiModelProperty(value = "创建时间")
//    private LocalDateTime createTime;
//
//    @ApiModelProperty(value = "更新人")
//    private String updateBy;
//
//    @ApiModelProperty(value = "更新时间")
//    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<SysMenu> children;

//    @Override
//    protected Serializable pkVal() {
//        return this.id;
//    }

}
