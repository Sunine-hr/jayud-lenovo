package com.jayud.wms.model.po;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryDetail 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="库存明细表对象", description="库存明细表")
@TableName(value = "wms_inventory_detail")
public class InventoryDetail extends SysBaseEntity {


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

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "生产日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime materialProductionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String customField1;

    @ApiModelProperty(value = "自定义字段2")
    private String customField2;

    @ApiModelProperty(value = "自定义字段3")
    private String customField3;

    @ApiModelProperty(value = "现有量")
    private BigDecimal existingCount;

    @ApiModelProperty(value = "分配量")
    private BigDecimal allocationCount;

    @ApiModelProperty(value = "拣货量")
    private BigDecimal pickingCount;

    @ApiModelProperty(value = "备注信息")
    private String remark;

    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @TableField(exist = false)
    @ApiModelProperty(value = "可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段")
    private BigDecimal usableCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "所需数量")
    private BigDecimal needCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "库位状态(false 未冻结; true 已冻结)")
    private Boolean locationState;

    @TableField(exist = false)
    @ApiModelProperty(value = "查询库位状态(0 未冻结; 1 已冻结)")
    private Integer queryLocationState;

    @TableField(exist = false)
    @ApiModelProperty(value = "关键字(warehouse_location_code 库位编号; material_code 物料编号; )")
    private String keyword;

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

    @ApiModelProperty(value = "库位线路排序")
    @TableField(exist = false)
    private int routeSorting;

    @ApiModelProperty(value = "降序")
    @TableField(exist = false)
    private String descMsg;

    @ApiModelProperty(value = "升序")
    @TableField(exist = false)
    private String ascMsg;

    @ApiModelProperty(value = "条件参数")
    @TableField(exist = false)
    private String conditionParam;

    /**
     * 可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段
     * @return
     */
    public BigDecimal getUsableCount() {
        //可用量=现有量-分配量-拣货量
        usableCount = existingCount.subtract(allocationCount).subtract(pickingCount);
        return usableCount;
    }

    /**
     * 获取当前的可用量
     * @return
     */
    public BigDecimal getThisUsableCount() {
        return this.usableCount;
    }

    /**
     * 库位状态(false 未冻结; true 已冻结)
     * @return
     */
    public boolean getLocationState(){
        if(ObjectUtil.isEmpty(this.warehouseLocationStatus) || ObjectUtil.isEmpty(this.warehouseLocationStatus2)){
            locationState = false;//false 未冻结
        }else{
            if(this.warehouseLocationStatus == 1 || this.warehouseLocationStatus2 == 1){
                locationState = true;//true 已冻结
            }else{
                locationState = false;//false 未冻结
            }
        }
        return locationState;
    }


}
