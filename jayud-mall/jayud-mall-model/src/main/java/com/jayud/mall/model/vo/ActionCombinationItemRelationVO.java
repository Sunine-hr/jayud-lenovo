package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ActionCombinationItemRelationVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "操作项组合id(action_combination id)", position = 2)
    @JSONField(ordinal = 2)
    private Integer actionCombinationId;

    @ApiModelProperty(value = "操作项id(action_item id)", position = 3)
    @JSONField(ordinal = 3)
    private Integer actionItemId;

    @ApiModelProperty(value = "前缀说明(1提前 2延后)", position = 4)
    @JSONField(ordinal = 4)
    private Integer prefixDeclare;

    @ApiModelProperty(value = "时间数量", position = 5)
    @JSONField(ordinal = 5)
    private Integer timeNumber;

    @ApiModelProperty(value = "时间单位(1天 2周 3月 4年)", position = 6)
    @JSONField(ordinal = 6)
    private Integer timeUnit;

    @ApiModelProperty(value = "排序", position = 7)
    @JSONField(ordinal = 7)
    private Integer sort;

    /*操作项组合action_combination*/
    @ApiModelProperty(value = "操作项组合名称", position = 8)
    @JSONField(ordinal = 8)
    private String actionCombinationName;

    /*操作项action_item*/
    @ApiModelProperty(value = "操作项名称", position = 9)
    @JSONField(ordinal = 9)
    private String actionItemName;

}
