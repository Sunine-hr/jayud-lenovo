package com.jayud.wms.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货架示意图，库位物料明细（料号、数量）
 */
@Data
public class MaterialDetailVO {

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "物料编号")
    private String materialCode;

    @ApiModelProperty(value = "物料名称")
    private String materialName;

    @ApiModelProperty(value = "现有量")
    private BigDecimal existingCount;

    @ApiModelProperty(value = "分配量")
    private BigDecimal allocationCount;

    @ApiModelProperty(value = "拣货量")
    private BigDecimal pickingCount;

    @TableField(exist = false)
    @ApiModelProperty(value = "可用量(计算字段),可用量=现有量-分配量-拣货量，数据库不存在此字段")
    private BigDecimal usableCount;

    @ApiModelProperty(value = "盘点数量")
    private BigDecimal checkCount;


}
