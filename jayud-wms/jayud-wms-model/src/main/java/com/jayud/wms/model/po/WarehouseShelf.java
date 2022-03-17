package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * WarehouseShelf 实体类
 *
 * @author jyd
 * @since 2022-03-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="货架信息对象", description="货架信息")
@TableName(value = "wms_warehouse_shelf")
public class WarehouseShelf extends SysBaseEntity {


    @ApiModelProperty(value = "所属仓库id")
    private Long warehouseId;

    @ApiModelProperty(value = "所属仓库库区id")
    private Long warehouseAreaId;

    @ApiModelProperty(value = "货架编号")
    private String code;

    @ApiModelProperty(value = "货架类型code(TC01立体货架,TC02平铺货架)")
    private String typeCode;

    @ApiModelProperty(value = "货架类型desc")
    private String typeDesc;

    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private Boolean status;

    @TableField(exist = false)
    @ApiModelProperty(value = "状态（0 禁用 1启用）")
    private String statusDesc;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;

    @ApiModelProperty(value = "货架位置状态(1在固定位 2在移动中 3在工作台)")
    private Integer locationStatus;

    @ApiModelProperty(value = "仓库编号")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "库区编号")
    private String warehouseAreaCode;

    @ApiModelProperty(value = "库区名称")
    private String warehouseAreaName;


    /************************* 惠州道科-业务 统计计算字段 @MFC 2022年3月5日09:41:49 *************************************/
    @TableField(exist = false)
    @ApiModelProperty(value = "总库位容量")
    private BigDecimal totalLocationCapacity;

    @TableField(exist = false)
    @ApiModelProperty(value = "已使用的库位容量")
    private BigDecimal usedLocationCapacity;

    @TableField(exist = false)
    @ApiModelProperty(value = "可用库位容量")
    private BigDecimal availableLocationCapacity;

    public void setStatus(Boolean status) {
        this.status = status;
        if(status){
            this.statusDesc = "是";
        }else{
            this.statusDesc = "否";
        }
    }
}
