package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.jayud.common.entity.SysBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * WmsOutboundShippingReviewInfoToMaterial 实体类
 *
 * @author jayud
 * @since 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="wms-出库发运复核物料信息对象", description="wms-出库发运复核物料信息")
public class WmsOutboundShippingReviewInfoToMaterial extends SysBaseEntity {


    @ApiModelProperty(value = "发运复核编号")
    private String shippingReviewOrderNumber;

    @ApiModelProperty(value = "出库单物料id")
    private Long orderMaterialId;

    @ApiModelProperty(value = "物料id")
    private Long materialId;

    @ApiModelProperty(value = "物料编码")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "物料规格")
    private String materialSpecification;

    @ApiModelProperty(value = "需求量")
    private BigDecimal account;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "外部单号")
    private String externalNumber;

    @ApiModelProperty(value = "外部单行号")
    private String externalLineNumber;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @ApiModelProperty(value = "生产时间")
    private LocalDateTime materialProductionDate;

    @ApiModelProperty(value = "自定义1")
    private String customField1;

    @ApiModelProperty(value = "自定义2")
    private String customField2;

    @ApiModelProperty(value = "自定义3")
    private String customField3;

    @ApiModelProperty(value = "状态(1未发运复核，2已发运复核)")
    private Boolean isReview;

    @ApiModelProperty(value = "容器id")
    private Long containerId;

    @ApiModelProperty(value = "容器编码")
    private String containerCode;

    @ApiModelProperty(value = "上传图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "备注")
    private String remark;


    @ApiModelProperty(value = "是否删除，0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "箱号")
    private Integer boxNumber;

    @ApiModelProperty(value = "出仓号")
    private String outWarehouseNumber;

    @ApiModelProperty(value = "入仓号")
    private String inWarehouseNumber;

    @ApiModelProperty(value = "复核状态文本")
    @TableField(exist = false)
    private String isReview_text;

    @JsonProperty(value = "weight")
    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @JsonProperty(value = "volume")
    @ApiModelProperty(value = "体积")
    private BigDecimal volume;


}
