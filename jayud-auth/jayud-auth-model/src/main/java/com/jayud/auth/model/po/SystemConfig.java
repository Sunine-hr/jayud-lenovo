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
 * SystemConfig 实体类
 *
 * @author jayud
 * @since 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统配置表对象", description="系统配置表")
public class SystemConfig extends SysBaseEntity {


    @ApiModelProperty(value = "系统配置code")
    private String configCode;

    @ApiModelProperty(value = "字段1")
    private String temp1;

    @ApiModelProperty(value = "字段2")
    private String temp2;

    @ApiModelProperty(value = "字段3")
    private String temp3;

    @ApiModelProperty(value = "字段4")
    private String temp4;

    @ApiModelProperty(value = "字段5")
    private String temp5;

    @ApiModelProperty(value = "字段6")
    private String temp6;

    @ApiModelProperty(value = "字段7")
    private String temp7;

    @ApiModelProperty(value = "字段8")
    private String temp8;

    @ApiModelProperty(value = "字段9")
    private String temp9;

    @ApiModelProperty(value = "字段10")
    private String temp10;

    @ApiModelProperty(value = "是否同步在线")
    private Boolean ifOnl;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "备注")
    private LocalDateTime remark;

    @ApiModelProperty(value = "组织机构ID")
    private Long orgId;

    @ApiModelProperty(value = "多租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "多租户code")
    private String tenantCode;

    @ApiModelProperty(value = "删除标志")
    @TableLogic
    private Boolean isDeleted;
    @ApiModelProperty(value = "删除人")
    private Long deletedUserId;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "删除时间")
    private LocalDateTime deleteUserName;


}
