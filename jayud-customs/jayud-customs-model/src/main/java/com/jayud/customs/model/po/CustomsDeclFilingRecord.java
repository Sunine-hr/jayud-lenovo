package com.jayud.customs.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报关归档记录
 * </p>
 *
 * @author LDR
 * @since 2021-08-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CustomsDeclFilingRecord对象", description = "报关归档记录")
public class CustomsDeclFilingRecord extends Model<CustomsDeclFilingRecord> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "报关归档id")
    private Long customsDeclFilingId;

    @ApiModelProperty(value = "云报关号")
    private String num;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
