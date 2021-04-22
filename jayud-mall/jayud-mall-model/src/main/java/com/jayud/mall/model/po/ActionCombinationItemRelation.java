package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 操作组合操作项目关联表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ActionCombinationItemRelation对象", description="操作组合操作项目关联表")
public class ActionCombinationItemRelation extends Model<ActionCombinationItemRelation> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
