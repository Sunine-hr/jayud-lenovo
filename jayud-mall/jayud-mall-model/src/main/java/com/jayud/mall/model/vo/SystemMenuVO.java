package com.jayud.mall.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ApiModelProperty(value = "ID", position = 1)
    private Integer id;

    @ApiModelProperty(value = "父级ID", position = 2)
    private Integer parentId;

    @ApiModelProperty(value = "创建时间", position = 3)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "菜单名称", position = 4)
    private String name;

    @ApiModelProperty(value = "菜单级数", position = 5)
    private Integer level;

    @ApiModelProperty(value = "菜单排序", position = 6)
    private Integer sort;

    @ApiModelProperty(value = "前端名称", position = 7)
    private String key;

    @ApiModelProperty(value = "前端图标", position = 8)
    private String icon;

    @ApiModelProperty(value = "前端隐藏 0显示,1隐藏", position = 9)
    private Integer hidden;

    @ApiModelProperty(value = "前端路由", position = 10)
    private String router;

    @ApiModelProperty(value = "子节点", position = 11)
    private List<SystemMenuVO> children;

}
