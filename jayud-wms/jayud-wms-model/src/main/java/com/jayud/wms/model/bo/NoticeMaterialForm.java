package com.jayud.wms.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * NoticeMaterial 实体类
 *
 * @author jyd
 * @since 2021-12-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "NoticeMaterial对象", description = "通知单物料信息")
public class NoticeMaterialForm extends SysBaseEntity {


    @ApiModelProperty(value = "通知单id")
    private Long receiptNoticeId;

    @ApiModelProperty(value = "通知单号")
    private String receiptNoticeNum;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料类型id")
    private Long materialTypeId;

    @ApiModelProperty(value = "物料类型")
    private String materialType;

    @ApiModelProperty(value = "重量")
    private BigDecimal weight = new BigDecimal(0);

    @ApiModelProperty(value = "体积")
    private BigDecimal volume = new BigDecimal(0);

    @ApiModelProperty(value = "物料规格")
    private String specification;

    @ApiModelProperty(value = "数量")
    private Double num = 0.0;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "外部订单号")
    private String externalOrderNum;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNum;

    @ApiModelProperty(value = "批次号")
    private String batchNum;

    @ApiModelProperty(value = "生产日期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private LocalDate productionDate;

    @ApiModelProperty(value = "自定义字段1")
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
    private Boolean isDeleted;


}
