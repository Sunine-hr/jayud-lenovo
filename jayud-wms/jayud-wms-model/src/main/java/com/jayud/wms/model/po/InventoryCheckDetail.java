package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * InventoryCheckDetail 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="库存盘点明细表对象", description="库存盘点明细表")
@TableName(value = "wms_inventory_check_detail")
public class InventoryCheckDetail extends SysBaseEntity {


    @ApiModelProperty(value = "库存盘点ID")
    private Long inventoryCheckId;

    @ApiModelProperty(value = "库存明细ID")
    private Long inventoryDetailId;

    @ApiModelProperty(value = "明细盘点状态(1未盘点、2已盘点、3已过账)")
    private Integer checkStatus;

    @ApiModelProperty(value = "货主ID")
    private Long owerId;

    @ApiModelProperty(value = "货主编号")
    private String owerCode;

    @ApiModelProperty(value = "货主名称")
    private String owerName;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

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

    @ApiModelProperty(value = "库存数量")
    private BigDecimal inventoryCount;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal checkCount;

    @ApiModelProperty(value = "盘盈数量")
    private BigDecimal checkSurplusCount;

    @ApiModelProperty(value = "盘亏数量")
    private BigDecimal checkLossesCount;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;






}
