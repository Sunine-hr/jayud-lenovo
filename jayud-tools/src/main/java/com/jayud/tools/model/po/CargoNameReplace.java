package com.jayud.tools.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 货物名称替换表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="CargoNameReplace对象", description="货物名称替换表")
public class CargoNameReplace extends Model<CargoNameReplace> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID", position = 1)
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(ordinal = 1)
    private Long id;

    @ApiModelProperty(value = "(原)货品名称", position = 2)
    @JSONField(ordinal = 2)
    private String hpmc;

    @ApiModelProperty(value = "替换名称", position = 3)
    @TableField("replaceName")
    @JSONField(ordinal = 3)
    private String replaceName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
