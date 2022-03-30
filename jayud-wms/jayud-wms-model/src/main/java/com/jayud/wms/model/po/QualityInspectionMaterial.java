package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * QualityInspectionMaterial 实体类
 *
 * @author jyd
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "质检物料信息对象", description = "质检物料信息")
@TableName(value = "wms_quality_inspection_material")
public class QualityInspectionMaterial extends SysBaseEntity {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "质检表id")
    private Long qualityInspectionId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "预期数量")
    private Double num;

    @ApiModelProperty(value = "实收数量")
    private Double actualNum;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "物料规格")
    private String specification;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "外部订单号")
    private String externalOrderNum;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNum;

    @ApiModelProperty(value = "自定义字段1(是否合格,可能后续上架策略会使用)")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    //@TableLogic //@TableLogic注解表示逻辑删除,设置修改人手动更新，这里注释
    private Boolean isDeleted;


    @ApiModelProperty(value = "检验数量")
    private Integer inspectionQuantity;

    @ApiModelProperty(value = "合格数量")
    private Integer qualifiedQuantity;

    @ApiModelProperty(value = "不合格数量")
    private Integer unqualifiedQuantity;

    @ApiModelProperty(value = "不合格原因（字典值）")
    private String causeNonconformity;

    @ApiModelProperty(value = "上传文件")
    private String description;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "合格率")
    private String percentOfPass;


}
