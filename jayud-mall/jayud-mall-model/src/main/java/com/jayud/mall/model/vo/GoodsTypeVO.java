package com.jayud.mall.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsTypeVO {

    @ApiModelProperty(value = "自增加")
    private Long id;

    @ApiModelProperty(value = "货物类型名")
    @TableField(value = "`name`")
    private String name;

    @ApiModelProperty(value = "父级id")
    private Integer fid;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用")
    @TableField(value = "`status`")
    private String status;

    @ApiModelProperty(value = "类型1报价类型 2货物类型")
    private String types;

}
