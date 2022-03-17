package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.List;

/**
 * WmsMaterialBasicInfo 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="物料基本信息对象", description="物料基本信息")
public class WmsMaterialBasicInfo extends SysBaseEntity {


    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "货主id")
    private Long owerId;

    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料组id")
    private Long materialGroupId;

    @ApiModelProperty(value = "ABC标识id")
    private Long identificationId;

    @ApiModelProperty(value = "冷热度")
    private String heatRange;

    @ApiModelProperty(value = "储存条件id")
    private Long storageConditionsId;

    @ApiModelProperty(value = "最小单位id")
    private Long minUnitId;

    @ApiModelProperty(value = "重量")
    @Digits(integer = 10, fraction = 2, message = "重量保留小数点后两位！")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    @Digits(integer = 10, fraction = 2, message = "体积保留小数点后两位！")
    private BigDecimal volume;

    @ApiModelProperty(value = "库存上限")
    private BigDecimal maxInventory;

    @ApiModelProperty(value = "库存下限")
    private BigDecimal mminInventory;

    @ApiModelProperty(value = "质保期(天)")
    private BigDecimal warrantyPeriod;

    @ApiModelProperty(value = "是否质检，0否，1是")
    private Boolean isQualityInspection;

    @ApiModelProperty(value = "是否换容器，0否，1是")
    private Boolean isReplaceContainer;

    @ApiModelProperty(value = "是否开启，0否，1是")
    private Boolean isOn;

    @ApiModelProperty(value = "备用字段1")
    private String columnOne;

    @ApiModelProperty(value = "备用字段2")
    private String columnTwo;

    @ApiModelProperty(value = "备用字段3")
    private String columnThree;

    @ApiModelProperty(value = "备用字段4")
    private String columnFour;

    @ApiModelProperty(value = "备用字段5")
    private String columnFive;

    @ApiModelProperty(value = "备用字段6")
    private String columnSix;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "上架策略id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long shelfStrategyId;

    @ApiModelProperty(value = "分配策略id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long allocationStrategyId;

    @ApiModelProperty(value = "是否允许超收，0否，1是")
    private Boolean isAllowOvercharge;

    @ApiModelProperty(value = "超收比例")
    private BigDecimal overchargeRatio;

    @ApiModelProperty(value = "周转方式(1先进先出,2后进先出)")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer turnoverMode;

    @ApiModelProperty(value = "周转批属性(1批次号,2生产日期,3字段1,4字段2,4字段3)")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer turnoverAttribute;

    @ApiModelProperty(value = "上架推荐仓库id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recommendedWarehouseId;

    @ApiModelProperty(value = "上架推荐仓库编号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recommendedWarehouseCode;

    @ApiModelProperty(value = "上架推荐仓库名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recommendedWarehouseName;

    @ApiModelProperty(value = "上架推荐库区id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recommendedWarehouseAreaId;

    @ApiModelProperty(value = "上架推荐库区编号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recommendedWarehouseAreaCode;

    @ApiModelProperty(value = "上架推荐库区名称")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recommendedWarehouseAreaName;


    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @TableField(exist = false)
    private List<Long> idList;






}
