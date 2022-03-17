package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryMovementTask 实体类
 *
 * @author jyd
 * @since 2021-12-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="库存移库任务对象", description="库存移库任务")
@TableName(value = "wms_inventory_movement_task")
public class InventoryMovementTask extends SysBaseEntity {


    @ApiModelProperty(value = "移库类型代码(1物料移库，2容器移库，3库位移库)")
    private Integer movementTypeCode;

    @ApiModelProperty(value = "移库类型名称")
    private String movementTypeName;

    @ApiModelProperty(value = "主任务号")
    private String mainCode;

    @ApiModelProperty(value = "明细任务号")
    private String detailCode;

    @ApiModelProperty(value = "任务状态代码(1待移库，2已移库)")
    private Integer taskStatusCode;

    @ApiModelProperty(value = "任务状态名称")
    private String taskStatusName;

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
    private Long warehouseAreaId;

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

    @ApiModelProperty(value = "现有量")
    private BigDecimal existingCount;

    @ApiModelProperty(value = "可用量(计算字段，可用量=现有量-分配量-拣货量)")
    private BigDecimal usableCount;

    @ApiModelProperty(value = "分配量")
    private BigDecimal allocationCount;

    @ApiModelProperty(value = "拣货量")
    private BigDecimal pickingCount;

    @ApiModelProperty(value = "目标库位ID")
    private Long toWarehouseLocationId;

    @ApiModelProperty(value = "目标库位编号")
    private String toWarehouseLocationCode;

    @ApiModelProperty(value = "目标容器ID")
    private Long toContainerId;

    @ApiModelProperty(value = "目标容器号")
    private String toContainerCode;

    @ApiModelProperty(value = "操作数量(移动数量)")
    private BigDecimal toOperationCount;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    //公司
    @TableField(exist = false)
    private List<String> orgIds;

    //是否在这个体系内
    @TableField(exist = false)
    private Boolean isCharge;

    //货主id
    @TableField(exist = false)
    private List<String> owerIdList;

    //仓库id
    @TableField(exist = false)
    private List<String> warehouseIdList;




}
