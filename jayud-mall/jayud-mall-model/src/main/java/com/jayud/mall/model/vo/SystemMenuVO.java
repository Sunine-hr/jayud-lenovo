package com.jayud.mall.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author mfc
 * @description:
 * @date 2020/10/24 13:49
 */
@Data
public class SystemMenuVO {

    @ApiModelProperty(value = "ID")
    private Integer id;

    @ApiModelProperty(value = "父级ID")
    private Integer parentId;

    private LocalDateTime createTime;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "菜单级数")
    private Integer level;

    @ApiModelProperty(value = "菜单排序")
    private Integer sort;

    @ApiModelProperty(value = "前端名称")
    private String key;

    @ApiModelProperty(value = "前端图标")
    private String icon;

    @ApiModelProperty(value = "前端隐藏 0显示,1隐藏")
    private Integer hidden;

    @ApiModelProperty(value = "前端路由")
    private String router;

    @ApiModelProperty(value = "子节点")
    private List<SystemMenuVO> children;

}
