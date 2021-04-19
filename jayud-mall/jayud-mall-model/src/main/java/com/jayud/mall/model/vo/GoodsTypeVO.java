package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsTypeVO {

    @ApiModelProperty(value = "自增id", position = 1)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "货物类型名", position = 2)
    @TableField(value = "`name`")
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "父级id(goods_type id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer fid;

    @ApiModelProperty(value = "创建人(system_user id)", position = 4)
    @JSONField(ordinal = 4)
    private String createBy;

    @ApiModelProperty(value = "创建时间", position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 5, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新人(system_user id)", position = 6)
    @JSONField(ordinal = 6)
    private String updateBy;

    @ApiModelProperty(value = "更新时间", position = 7)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 7, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用", position = 8)
    @TableField(value = "`status`")
    @JSONField(ordinal = 8)
    private String status;

    @ApiModelProperty(value = "类型1普货 2特货", position = 9)
    @JSONField(ordinal = 9)
    private String types;

}
