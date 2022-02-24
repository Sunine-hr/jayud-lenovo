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
 * SysEmamilConfig 实体类
 *
 * @author jayud
 * @since 2022-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="系统邮箱配置表对象", description="系统邮箱配置表")
public class SysEmamilConfig extends SysBaseEntity {


    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "发送服务器地址")
    private String emailServerAddr;

    @ApiModelProperty(value = "发送邮箱账号")
    private String sendEmailAccount;

    @ApiModelProperty(value = "发送邮箱账号名称")
    private String sendEmailAccountName;

    @ApiModelProperty(value = "邮箱密码")
    private String sendEmailAccountPassword;

    @ApiModelProperty(value = "财务邮箱地址")
    private String financialEmailAddr;

    @ApiModelProperty(value = "关务邮箱地址")
    private String affairsEmailAddr;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
