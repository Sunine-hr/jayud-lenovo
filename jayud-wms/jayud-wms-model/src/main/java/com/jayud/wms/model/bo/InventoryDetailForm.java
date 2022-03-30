package com.jayud.wms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryDetailForm extends SysBaseEntity {


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

    @ApiModelProperty(value = "库位状态-盘点(0未冻结 1已冻结)")
    private Integer warehouseLocationStatus;

    @ApiModelProperty(value = "库位状态2-移库(0未冻结 1已冻结)")
    private Integer warehouseLocationStatus2;

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

//    @ApiModelProperty(value = "现有量")
//    private BigDecimal existingCount;
//
//    @ApiModelProperty(value = "分配量")
//    private BigDecimal allocationCount;
//
//    @ApiModelProperty(value = "拣货量")
//    private BigDecimal pickingCount;
//
//    @ApiModelProperty(value = "备注信息")
//    private String remark;

    @ApiModelProperty(value = "操作数量（入库，出库）")
    private BigDecimal operationCount;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "单位")
    private String unit;


//    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
//    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
//    private Boolean isDeleted;

//    @TableField(exist = false)
//    @ApiModelProperty(value = "可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段")
//    private BigDecimal usableCount;

//    /**
//     * 可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
//     * @return
//     */
//    public BigDecimal getUsableCount() {
//        //可用量=现有量-分配量-拣货量
//        usableCount = existingCount.subtract(allocationCount).subtract(pickingCount);
//        return usableCount;
//    }


}
