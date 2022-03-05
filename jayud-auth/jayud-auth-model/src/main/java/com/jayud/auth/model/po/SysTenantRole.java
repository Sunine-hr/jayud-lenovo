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
 * SysTenantRole 实体类
 *
 * @author jayud
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="租户规则配置对象", description="租户规则配置")
public class SysTenantRole extends SysBaseEntity {


    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "是否显示公海客户")
    private Boolean isShowCrmPublic;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
