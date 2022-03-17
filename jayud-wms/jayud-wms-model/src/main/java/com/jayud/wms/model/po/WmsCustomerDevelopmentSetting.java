package com.jayud.wms.model.po;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Blob;
import java.time.LocalDateTime;

/**
 * WmsCustomerDevelopmentSetting 实体类
 *
 * @author jyd
 * @since 2022-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="客户开发设置表对象", description="客户开发设置表")
public class WmsCustomerDevelopmentSetting extends SysBaseEntity {


    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "应用密钥")
    private String appSecret;

    @ApiModelProperty(value = "启用状态：0禁用，1启用")
    private Integer status;

    @ApiModelProperty(value = "类型：supplierType/commonCarrier/consignee")
    private String type;

    @ApiModelProperty(value = "主体ID(wms_customer_info.id)")
    private Long mainId;

    @ApiModelProperty(value = "供应商服务IP")
    private String supplierServerIp;

    @ApiModelProperty(value = "供应商服务端口")
    private String supplierServerPort;

    @ApiModelProperty(value = "供应商文件服地址(outboundfillepath)")
    private String supplierFilePath;

    @ApiModelProperty(value = "供应商反馈信息地址")
    private String supplierCallbackAddress;

    @ApiModelProperty(value = "外部接口调用时供应商的系统登录用户名")
    private String supplierUser;

    @ApiModelProperty(value = "登录密码")
    private String supplierPassword;

    @ApiModelProperty(value = "供应商时区")
    private Integer timeZone;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "开始时间：定时抓取的开始时间，为空则运行时触发")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "定时抓取时间间隔（为空表示每日只在指定时间刷新，开始和间隔不能同时为空）")
    private Integer intervalTime;

    @ApiModelProperty(value = "定时抓取时间间隔单位")
    private String intervalUnit;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "结束时间：定时抓取于每天的何时停止，为空则不停止")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "请求参数类型：xml，json")
    private String codeType;

    @ApiModelProperty(value = "请求参数内容")
    private Blob requestContent;

    @ApiModelProperty(value = "印度值为1，其他为0")
    private Integer flag;

    @ApiModelProperty(value = "")
    private String inboundPath;

    @ApiModelProperty(value = "库存地址")
    private String inventoryPath;

    @ApiModelProperty(value = "rsa加密公钥")
    private String publicKey;

    @ApiModelProperty(value = "rsa加密私钥")
    private String privateKey;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
