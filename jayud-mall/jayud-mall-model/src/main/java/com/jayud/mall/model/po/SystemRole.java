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
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-10-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SystemRole对象", description="系统角色表")
public class SystemRole extends Model<SystemRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Integer id;

    @ApiModelProperty(value = "角色名称", position = 2)
    @JSONField(ordinal = 2)
    private String roleName;

    @ApiModelProperty(value = "角色描述", position = 3)
    @JSONField(ordinal = 3)
    private String roleDescribe;

    @ApiModelProperty(value = "创建人", position = 4)
    @JSONField(ordinal = 4)
    private String createBy;

    @ApiModelProperty(value = "创建时间", position = 5)
    @JSONField(ordinal = 5)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人", position = 6)
    @JSONField(ordinal = 6)
    private String updateBy;

    @ApiModelProperty(value = "修改时间", position = 7)
    @JSONField(ordinal = 7)
    private LocalDateTime updateTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
