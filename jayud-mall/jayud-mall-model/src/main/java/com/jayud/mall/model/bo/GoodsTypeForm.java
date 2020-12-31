package com.jayud.mall.model.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "(报价&货物)类型表")
public class GoodsTypeForm {

    @ApiModelProperty(value = "自增id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "货物类型名", position = 2)
    @TableField(value = "`name`")
    @JSONField(ordinal = 2)
    private String name;

    @ApiModelProperty(value = "父级id(goods_type id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer fid;

    @ApiModelProperty(value = "启用状态0-禁用，1-启用", position = 4)
    @TableField(value = "`status`")
    @JSONField(ordinal = 4)
    private String status;

    @ApiModelProperty(value = "类型1普货 2特货", position = 5)
    @JSONField(ordinal = 5)
    private String types;


}
