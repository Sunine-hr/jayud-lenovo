package com.jayud.auth.model.vo;

import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysLog 实体类
 *
 * @author jayud
 * @since 2022-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统日志表对象", description="系统日志表")
public class SysLogVO extends SysBaseEntity {


    @ApiModelProperty(value = "日志类型（1登录日志，2操作日志）")
    private Integer logType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "日志内容")
    private String logContent;

    @ApiModelProperty(value = "操作类型(0:系统,1:客户)")
    private Integer operateType;

    @ApiModelProperty(value = "操作用户账号")
    private String userid;

    @ApiModelProperty(value = "操作用户名称")
    private String username;

    @ApiModelProperty(value = "用户真实姓名")
    private String trueName;

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
