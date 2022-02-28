package com.jayud.auth.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * SysArea 实体类
 *
 * @author jayud
 * @since 2022-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统-中国行政地区表对象", description="系统-中国行政地区表")
public class SysArea extends SysBaseEntity {


    @ApiModelProperty(value = "层级（0-省，1-市，2-区）")
    private Integer level;

    @ApiModelProperty(value = "行政代码")
    private Long code;

    @ApiModelProperty(value = "父级行政代码")
    private Long parentCode;

    @ApiModelProperty(value = "邮政编码")
    private Integer zipCode;

    @ApiModelProperty(value = "区号")
    private String cityCode;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "简称")
    private String shortName;

    @ApiModelProperty(value = "组合名")
    private String mergerName;

    @ApiModelProperty(value = "拼音")
    private String pinyin;

    @ApiModelProperty(value = "经度")
    private BigDecimal lng;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;






    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

}
