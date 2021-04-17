package com.jayud.mall.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作项
 * </p>
 *
 * @author fachang.mao
 * @since 2020-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="ActionItem对象", description="操作项")
public class ActionItem extends Model<ActionItem> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "操作项名称", position = 2)
    @JSONField(ordinal = 2)
    private String itemName;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
