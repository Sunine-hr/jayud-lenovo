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
 * SysUrl 实体类
 *
 * @author jayud
 * @since 2022-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统链接表对象", description="系统链接表")
public class SysUrl extends SysBaseEntity {


    @ApiModelProperty(value = "系统名称")
    private String name;

    @ApiModelProperty(value = "系统链接")
    private String url;

    @ApiModelProperty(value = "系统类型(1-权限系统，2-OMS系统)")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;






    }
