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
 * SysLog 实体类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统日志表对象", description="系统日志表")
public class SysLog extends SysBaseEntity {


    @ApiModelProperty(value = "日志类型（1登录日志，2操作日志）")
    private Integer logType;

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "操作类型")
    private Integer operateType;

    @ApiModelProperty(value = "操作用户账号")
    private String userid;

    @ApiModelProperty(value = "操作用户名称")
    private String username;

    @ApiModelProperty(value = "IP")
    private String ip;

    @ApiModelProperty(value = "请求java方法")
    private String method;

    @ApiModelProperty(value = "请求路径")
    private String requestUrl;

    @ApiModelProperty(value = "请求参数")
    private String requestParam;

    @ApiModelProperty(value = "请求类型")
    private String requestType;

    @ApiModelProperty(value = "耗时")
    private Long costTime;






}
