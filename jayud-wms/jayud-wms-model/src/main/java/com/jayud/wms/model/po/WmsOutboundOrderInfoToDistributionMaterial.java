package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
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
import java.util.List;

/**
 * WmsOutboundOrderInfoToDistributionMaterial 实体类
 *
 * @author jyd
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="出库单-分配物料信息对象", description="出库单-分配物料信息")
public class WmsOutboundOrderInfoToDistributionMaterial extends SysBaseEntity {


    @ApiModelProperty(value = "出库单物料信息id")
    private Long orderMaterialId;

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

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;


    @ApiModelProperty(value = "库区ID")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "库位ID")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "容器id")
    private Long containerId;

    @ApiModelProperty(value = "容器号")
    private String containerCode;

    @ApiModelProperty(value = "分配量")
    private BigDecimal distributionAccount;

    @ApiModelProperty(value = "实际分配量")
    private BigDecimal realDistributionAccount;

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

    @ApiModelProperty(value = "分配-批次号")
    private String distributionBatchCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "分配-生产时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime distributionMaterialProductionDate;

    @ApiModelProperty(value = "分配-自定义1")
    private String distributionCustomField1;

    @ApiModelProperty(value = "分配-自定义2")
    private String distributionCustomField2;

    @ApiModelProperty(value = "分配-自定义3")
    private String distributionCustomField3;

    //惠州道科
    @ApiModelProperty(value = "所属仓库货架id")
    private Long shelfId;

    //惠州道科
    @ApiModelProperty(value = "所属货架code")
    private String shelfCode;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    private List<Long> idList;






}
