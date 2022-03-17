package com.jayud.wms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存移动
 * 原库位 -->  目标库位
 */
@Data
public class InventoryMovementForm {


//    @ApiModelProperty(value = "事务类型代码")
//    private Integer businessTypeCode;
//
//    @ApiModelProperty(value = "事务类型名称")
//    private String businessTypeName;

    //原库位信息
    @ApiModelProperty(value = "原库存明细ID")
    private Long inventoryDetailId;

    @ApiModelProperty(value = "货主ID")
    private Long owerId;

    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "原仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "原仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "原仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "原库区ID")
    private String warehouseAreaId;

    @ApiModelProperty(value = "原库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "原库区名称")
    private String warehouseAreaName;

    @ApiModelProperty(value = "原库位ID")
    private Long warehouseLocationId;

    @ApiModelProperty(value = "原库位编号")
    private String warehouseLocationCode;

    @ApiModelProperty(value = "原容器id")
    private Long containerId;

    @ApiModelProperty(value = "原容器号")
    private String containerCode;

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料类型ID")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "物料规格")
    private String materialSpecification;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生产日期")
    private LocalDateTime materialProductionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String customField1;

    @ApiModelProperty(value = "自定义字段2")
    private String customField2;

    @ApiModelProperty(value = "自定义字段3")
    private String customField3;

    @ApiModelProperty(value = "原数量(原现有量)")
    private BigDecimal existingCount;

    //目标库位信息
    @ApiModelProperty(value = "目标库位ID")
    private Long toWarehouseLocationId;

    @ApiModelProperty(value = "目标库位编号")
    private String toWarehouseLocationCode;

    @ApiModelProperty(value = "目标容器ID")
    private Long toContainerId;

    @ApiModelProperty(value = "目标容器号")
    private String toContainerCode;

    //操作数量
    @ApiModelProperty(value = "操作数量(移动数量)")
    private BigDecimal toOperationCount;

    @ApiModelProperty(value = "备注信息")
    private String remark;

}
