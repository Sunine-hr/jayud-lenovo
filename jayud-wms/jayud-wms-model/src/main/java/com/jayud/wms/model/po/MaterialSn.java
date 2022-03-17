package com.jayud.wms.model.po;

import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * MaterialSn 实体类
 *
 * @author jyd
 * @since 2021-12-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "物料sn信息对象", description = "物料sn信息")
@TableName(value = "wms_material_sn")
public class MaterialSn extends SysBaseEntity {


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

    @ApiModelProperty(value = "重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "物料规格")
    private String specification;

    @ApiModelProperty(value = "预期数量")
    private Double num;

    @ApiModelProperty(value = "单位")
    private String unit;

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

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "备注信息")
    private String remark;


    @ApiModelProperty(value = "是否删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "收货状态(1:未收,2:收货中,3:已收货,4:撤销)")
    private Integer status;

    @ApiModelProperty(value = "是否直接上架")
    private Boolean isPutShelf;
}
