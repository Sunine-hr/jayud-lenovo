package com.jayud.mall.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActionCombinationVO {

    @ApiModelProperty(value = "主键id", position = 1)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "操作项组合名称", position = 2)
    @JSONField(ordinal = 2)
    private String combinationName;

    @ApiModelProperty(value = "状态(0无效 1有效)", position = 3)
    @JSONField(ordinal = 3)
    private String status;

    @ApiModelProperty(value = "创建用户id(system_user id)", position = 4)
    @JSONField(ordinal = 4)
    private Integer userId;

    @ApiModelProperty(value = "创建用户名(system_user name)", position = 5)
    @JSONField(ordinal = 5)
    private String userName;

    @ApiModelProperty(value = "创建时间", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @JSONField(ordinal = 6, format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /*操作组合操作项目关联表action_combination_item_relation*/
    @ApiModelProperty(value = "操作组合关联操作项list", position = 7)
    @JSONField(ordinal = 7)
    private List<ActionCombinationItemRelationVO> actionCombinationItemRelationVOList;

}
