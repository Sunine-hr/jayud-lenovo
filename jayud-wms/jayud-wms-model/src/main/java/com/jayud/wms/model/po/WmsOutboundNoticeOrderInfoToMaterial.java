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
 * WmsOutboundNoticeOrderInfoToMaterial 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库通知订单-物料信息对象", description="出库通知订单-物料信息")
public class WmsOutboundNoticeOrderInfoToMaterial extends SysBaseEntity {


    @ApiModelProperty(value = "出库通知单号")
    private String orderNumber;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料规格")
    private String materialSpecification;

    @ApiModelProperty(value = "数量")
    private BigDecimal account;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "外部单号")
    private String externalNumber;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNumber;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "生产时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime materialProductionDate;

    @ApiModelProperty(value = "自定义1")
    private String customField1;

    @ApiModelProperty(value = "自定义2")
    private String customField2;

    @ApiModelProperty(value = "自定义3")
    private String customField3;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "出仓号")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "拣货下架库位id")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "拣货下架库位编码")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "库存明细id")
    private Long inventoryDetailId;

    @ApiModelProperty(value = "卡板id")
    private Long containerId;

    @ApiModelProperty(value = "卡板编码")
    private String containerCode;



}
