package com.jayud.wms.model.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Material 实体类
 *
 * @author
 * @since
 */
@Data
@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = false)
//@ApiModel(value = "货单物料信息对象", description = "货单物料信息")
public class QualityMaterialForm {


    @ApiModelProperty(value = "收货单id")
    private Long orderId;

    @ApiModelProperty(value = "收货单号")
    private String orderNum;

    @ApiModelProperty(value = "物料序列号(SN)")
    private String serialNum;

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

    @ApiModelProperty(value = "容器号")
    private String containerNum;

    @ApiModelProperty(value = "收货状态(1:未收,2:收货中,3:已收货,4:撤销)")
    private Integer status;

    @ApiModelProperty(value = "外部订单号")
    private String externalOrderNum;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNum;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1")
    private String columnOne;

    @ApiModelProperty(value = "自定义字段2")
    private String columnTwo;

    @ApiModelProperty(value = "自定义字段3")
    private String columnThree;



}
