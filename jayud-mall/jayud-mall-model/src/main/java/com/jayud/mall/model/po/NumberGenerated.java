package com.jayud.mall.model.po;

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
 * 单号生成器
 * </p>
 *
 * @author fachang.mao
 * @since 2020-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NumberGenerated对象", description="单号生成器")
public class NumberGenerated extends Model<NumberGenerated> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "单号编号(唯一约束，不可重复)")
    private String nameCode;

    @ApiModelProperty(value = "单号名称")
    private String nameText;

    @ApiModelProperty(value = "前缀")
    private String prefix;

    @ApiModelProperty(value = "后缀")
    private String suffix;

    @ApiModelProperty(value = "计数器")
    private Long count;

    @ApiModelProperty(value = "单号长度(最大10位数)")
    private Long numberLength;

    @ApiModelProperty(value = "单号格式(DATE_FORMAT(date,format))")
    private String numberFormat;

    @ApiModelProperty(value = "重置类型(0不重置 1按年重置 2按月重置 3按日重置)")
    private String types;

    @ApiModelProperty(value = "单号时间")
    private LocalDateTime numberTime;

    @ApiModelProperty(value = "状态(0停用 1启用)")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
