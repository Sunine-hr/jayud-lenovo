package com.jayud.mall.model.po;

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
 * 国家基础信息表
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Country对象", description="国家基础信息表")
public class Country extends Model<Country> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "国家名称")
    private String name;

    @ApiModelProperty(value = "国家简码")
    private String code;

    @ApiModelProperty(value = "地理位置")
    private String geo;

    @ApiModelProperty(value = "启用状态，1是0否")
    private String status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
