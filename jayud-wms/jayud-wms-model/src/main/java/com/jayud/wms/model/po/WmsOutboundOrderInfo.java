package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * WmsOutboundOrderInfo 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单对象", description="出库单")
public class WmsOutboundOrderInfo extends SysBaseEntity {


    @ApiModelProperty(value = "出库通知单号")
    private String noticeOrderNumber;

    @ApiModelProperty(value = "出库单号")
    private String orderNumber;

    @ApiModelProperty(value = "波次号")
    private String waveNumber;

    @ApiModelProperty(value = "仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "货主编码")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "单据类型(1采购入库单-原材料仓,2生产领料退货单-原材料仓,3半成品入库单-半成品仓,4生产领料退货单-半成品仓,5成品生产入库单-成品仓,6销售退货入库单)")
    private Integer documentType;

    @ApiModelProperty(value = "订单来源(1ERP下发,2EMS下发,3手工创建)")
    private Integer orderSourceType;

    @ApiModelProperty(value = "外部订单号1")
    private String externalOrderNumberFirst;

    @ApiModelProperty(value = "外部订单号2")
    private String externalOrderNumberSecond;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户编码")
    private String customerCode;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "预计出库时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime planDeliveryTime;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "合计数量")
    private BigDecimal allCount;

    @ApiModelProperty(value = "合计重量")
    private BigDecimal allHeight;

    @ApiModelProperty(value = "合计体积")
    private BigDecimal allVolume;

    @ApiModelProperty(value = "分配人")
    private String assignorBy;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "分配时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assignorTime;

    @ApiModelProperty(value = "订单状态(1未分配，2已分配，3缺货中，4已出库)")
    private Integer orderStatusType;

    @ApiModelProperty(value = "实际出库时间")
    private LocalDateTime realDeliveryTime;

    @ApiModelProperty(value = "物料总数")
    private BigDecimal materialAccount;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;






}
