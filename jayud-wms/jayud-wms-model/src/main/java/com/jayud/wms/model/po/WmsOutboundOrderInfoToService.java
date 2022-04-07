package com.jayud.wms.model.po;

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
 * WmsOutboundOrderInfoToService 实体类
 *
 * @author jayud
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单-附加服务对象", description="出库单-附加服务")
public class WmsOutboundOrderInfoToService extends SysBaseEntity {


    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "服务名称")
    private String serveTypeValue;

    @ApiModelProperty(value = "服务名称文本")
    private String serveTypeText;


    @ApiModelProperty(value = "数量")
    private Double account;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "单价")
    private Double unitPrice;

    @ApiModelProperty(value = "总价")
    private Double allPrice;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;





}
